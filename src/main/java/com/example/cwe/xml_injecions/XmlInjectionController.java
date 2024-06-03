package com.example.cwe.xml_injecions;

import com.example.cwe.xml_injecions.dto.LoginForm;
import com.example.cwe.xml_injecions.dto.User;
import com.example.cwe.xml_injecions.dto.Users;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@RestController
@RequestMapping("xmli")
public class XmlInjectionController {
    public static final String XML_PATH = "src/main/resources/xml/users.xml";

    @GetMapping("users")
    @ResponseStatus(HttpStatus.OK)
    public void getAllUsers(HttpServletResponse response) throws IOException {
        response.setContentType("text/xml");
        response.setCharacterEncoding(Charset.defaultCharset().name());
        response.getWriter().write(Files.readAllLines(Path.of(XML_PATH)).toString());
    }

    /**
     * Injection:
     * {
     * "username": "hacker</username><!--",
     * "password": "null",
     * "email": "--><password>xxx</password><group>Administrators</group><email>fu@qwe.asd"
     * }
     */
    @PostMapping(value = "add-user-unsafe"
            , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void addUserUnsafe(@RequestBody LoginForm loginForm, HttpServletResponse response) throws IOException {
        String newUser = "\t<user id=\"" + UUID.randomUUID() + "\">\n" +
                "\t\t<username>" + loginForm.username() + "</username>\n" +
                "\t\t<password>" + DigestUtils.md5Hex(loginForm.password()) + "</password>\n" +
                "\t\t<group>Users</group>\n" +
                "\t\t<email>" + loginForm.email() + "</email>\n" +
                "\t</user>";

        Path usersPath = Path.of(XML_PATH);
        String users = String.join("\n", Files.readAllLines(usersPath));
        String newUsers = users.replace("</users>", newUser) + "\n</users>";
        Files.writeString(usersPath, newUsers);

        response.setContentType("text/xml");
        response.getWriter().write(newUser);
    }

    @PostMapping(value = "add-user-safe",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void addUserSafe(@RequestBody LoginForm loginForm, HttpServletResponse response) throws IOException, JAXBException {

        JAXBContext context = JAXBContext.newInstance(Users.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        Users users = (Users) unmarshaller.unmarshal(new File(XML_PATH));

        User newUser = new User(loginForm.username(), DigestUtils.md5Hex(loginForm.password()), "Users"
                , UUID.randomUUID(), loginForm.email());

        users.addUser(newUser);

        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(users, new File(XML_PATH));

        response.setContentType("text/xml");
        marshaller.marshal(users, response.getOutputStream());
    }

    @PostMapping(value = "add-user-safe-escape",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void addUserSafeEscape(@RequestBody LoginForm loginForm, HttpServletResponse response) throws IOException {
        // Для экранирования спец символов используется org.apache.commons.text.StringEscapeUtils
        // Так же можно использовать org.springframework.web.util.HtmlUtils.htmlEscape()

        String newUser = "\t<user id=\"" + UUID.randomUUID() + "\">\n" +
                "\t\t<username>" + StringEscapeUtils.escapeXml11(loginForm.username()) + "</username>\n" +
                "\t\t<password>" + DigestUtils.md5Hex(loginForm.password()) + "</password>\n" +
                "\t\t<group>Users</group>\n" +
                "\t\t<email>" + StringEscapeUtils.escapeXml11(loginForm.email()) + "</email>\n" +
                "\t</user>";
        // Так же можно использовать org.springframework.web.util.HtmlUtils.htmlEscape()

        Path usersPath = Path.of(XML_PATH);
        String users = String.join("\n", Files.readAllLines(usersPath));
        String newUsers = users.replace("</users>", newUser) + "\n</users>";
        Files.writeString(usersPath, newUsers);

        response.setContentType("application/xml");
        response.getWriter().write(newUser);
    }

}

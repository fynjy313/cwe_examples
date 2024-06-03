package com.example.cwe.xml_injecions;

import com.example.cwe.xml_injecions.dto.User;
import com.example.cwe.xml_injecions.dto.Users;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.web.util.HtmlUtils;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

class XmlInjectionControllerTest {
    public static final String xmlPath = "src/main/resources/xml/users_copy.xml";

    @Test
    void getAllUsers() throws IOException {
        System.out.println(Files.readAllLines(Path.of(xmlPath)));
    }

    @Test
    public void positionTest() throws IOException {
// abrakodabra
        RandomAccessFile usersXml = new RandomAccessFile(xmlPath, "rw");
        System.out.println(usersXml.length());

        long position = usersXml.length() - 8;

        String text = "QWEQWEQWEQWE";

        usersXml.seek(position);
//        usersXml.writeUTF(text);
        usersXml.writeBytes(text);
        usersXml.writeUTF("\n</users>");

        usersXml.close();
    }

    @Test
    public void position2() throws IOException, JAXBException, ParserConfigurationException, SAXException {
//        String username = "qwe";
//        String password = "asd";
//        String email = "zxc@zxc";//
//        String newUser = "\t<user id=\"" + UUID.randomUUID() + "\">\n" +
//                "\t\t<username>" + username + "</username>\n" +
//                "\t\t<password>" + password + "</password>\n" +
//                "\t\t<group>Users</group>\n" +
//                "\t\t<email>" + email + "</email>\n" +
//                "\t</user>\n" + "</users>";//
//        Path usersPath = Path.of(xmlPath);
//        String users = String.join("", Files.readAllLines(usersPath));
//        String newUsers = users.replace("</users>", newUser);
//        Files.writeString(usersPath, newUsers);

//        File file = new File(xmlPath);


        JAXBContext jaxbContext = JAXBContext.newInstance(User.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        User user = new User("hacker</username><!--", DigestUtils.md5Hex("asd"), "gr"
                , UUID.randomUUID(), "--><password>xxx</password><group>Administrators</group><email>fu@qwe.asd");

        StringWriter stringWriter = new StringWriter();
        jaxbMarshaller.marshal(user, stringWriter);
        System.out.println(stringWriter);


        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(new File(xmlPath), new DefaultHandler());


    }

    @Test
    public void cwe73Test() throws IOException {
        String userInput = "C:\\temp\\tableScheme\\..\\";
        String userInput2 = "C:\\temp\\PPOD_object.txt\\%00\\tableScheme\\..\\";
        File file = new File(userInput + "PPOD_object.txt");
        System.out.println(file.getName());
        System.out.println(file.getCanonicalPath());
        System.out.println(file.getAbsolutePath());

        System.out.println(new File(userInput2).getAbsolutePath());

    }

    @Test
    public void testListMarshall() throws JAXBException {
        Users users = new Users();
        User user0 = new User("hacker</username><!--", DigestUtils.md5Hex("asd"), "gr"
                , UUID.randomUUID(), "--><password>xxx</password><group>Administrators</group><email>fu@qwe.asd");
        User user = new User("hacker", DigestUtils.md5Hex("asd"), "Users"
                , UUID.randomUUID(), "fu@qwe.asd");
        User user2 = new User("hacker2", DigestUtils.md5Hex("zxc"), "Users"
                , UUID.randomUUID(), "fu2@qwe.asd");
        users.addUser(user);
        users.addUser(user2);
        users.addUser(user0);

        JAXBContext context = JAXBContext.newInstance(Users.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        marshaller.marshal(users, new File("src/main/resources/xml/users_test.xml"));
    }

    @Test
    public void testListUnmarshall() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Users.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        Users users = (Users) unmarshaller.unmarshal(new File("src/main/resources/xml/users_test.xml"));

        System.out.println(users);
    }

    @Test
    public void testEscaping() {
        String xml = "hacker</username><!--    <>\"&'";

        System.out.println(HtmlUtils.htmlEscape(xml));

        System.out.println(StringEscapeUtils.escapeXml11(xml));


    }


}
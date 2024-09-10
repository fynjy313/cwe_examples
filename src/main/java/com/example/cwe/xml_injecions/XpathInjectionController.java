package com.example.cwe.xml_injecions;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * CWE-643: Improper Neutralization of Data within XPath Expressions ('XPath Injection')
 * <p>
 * Дополнительная литература
 * <a href="https://learn.snyk.io/lesson/xpath-injection/">...</a>
 * <a href="https://cqr.company/web-vulnerabilities/xpath-injection/">...</a>
 * <a href="https://www.netspi.com/blog/technical-blog/web-application-pentesting/exploiting-xpath-injection-weaknesses/">...</a>
 */
@RestController
@RequestMapping(value = "xpath")
public class XpathInjectionController {
    public static final String xsdPath = "src/main/resources/xml/users.xsd";
    public static final String xmlPath = "src/main/resources/xml/users.xml";

    //TODO: javadoc + example payload for all endpoints

    @GetMapping("resolve-user-group-unsafe")
    public void resolveUserGroupUnsafe(@RequestParam String username, @RequestParam String password
            , HttpServletResponse response)
            throws IOException, SAXException, XPathExpressionException, ParserConfigurationException {

        //Инъекции в поле login: ' or '1'='1' or ' | admin' or ' | 'or contains(.,'adm') or'

        DocumentBuilder documentBuilder = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(xmlPath);

        XPath xPath = XPathFactory.newInstance().newXPath();
        XPathExpression xlogin = xPath.compile("//users/user[username/text()='" + username +
                "'and password/text()='" + DigestUtils.md5Hex(password) + "']/group/text()");

        XPathExpression xlogin1 = xPath.compile("//users/user[username/text()='admin' or ''and password/text()='ee11cbb19052e40b07aac0ca060c23ee']/group/text()");

        XPathExpression xlogin2 = xPath.compile("//users/user[username/text()=''or contains(.,'adm') or''and password/text()='ee11cbb19052e40b07aac0ca060c23ee']/group/text()");

        String group = xlogin.evaluate(document);

        if (!group.isEmpty()) {
            response.getWriter().write("User group (found using XPath): " + group);
        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unknown user. Go away!");
    }

    @GetMapping("resolve-user-group-safe-param")
    public void resolveUserGroupSafeParam(@RequestParam String username, @RequestParam String password
            , HttpServletResponse response)
            throws IOException, SAXException, XPathExpressionException, ParserConfigurationException {
        //Для параметризации используется javax.xml.xpath.XPathVariableResolver

        DocumentBuilder documentBuilder = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(xmlPath);
        SimpleVariableResolver resolver = new SimpleVariableResolver();

        resolver.addVariable(new QName("username"), username);
        resolver.addVariable(new QName("password"), DigestUtils.md5Hex(password));

        XPath xPath = XPathFactory.newInstance().newXPath();
        xPath.setXPathVariableResolver(resolver);

        XPathExpression xlogin = xPath.compile("//users/user[username/text()=$username " +
                "and password/text()=$password]/group/text()");
        String group = xlogin.evaluate(document);

        if (!group.isEmpty()) {
            response.getWriter().write("User group (found using XPath): " + group);
        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unknown user. Go away!");
    }

    public static class SimpleVariableResolver implements XPathVariableResolver {
        private static final Map<QName, Object> vars = new HashMap<>();

        public void addVariable(QName name, Object value) {
            vars.put(name, value);
        }

        public Object resolveVariable(QName name) {
            return vars.get(name);
        }
    }

    @GetMapping("resolve-user-group-safe-sanitize")
    public void resolveUserGroupSafeSanitize(@RequestParam String username, @RequestParam String password
            , HttpServletResponse response)
            throws IOException, SAXException, XPathExpressionException, ParserConfigurationException {

        if (Pattern.matches("^.*\\W.*$", username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        } else {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(xmlPath);

            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression xlogin = xPath.compile("//users/user[username/text()='" + username +
                    "'and password/text()='" + DigestUtils.md5Hex(password) + "']/group/text()");
            String group = xlogin.evaluate(document);

            if (!group.isEmpty()) {
                response.getWriter().write("User group (found using XPath): " + group);
            } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unknown user. Go away!");
        }
    }

}
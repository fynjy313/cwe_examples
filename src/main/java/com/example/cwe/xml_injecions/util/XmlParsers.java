package com.example.cwe.xml_injecions.util;

import com.example.cwe.xml_injecions.pojo.User;
import com.example.cwe.xml_injecions.pojo.Users;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.util.UUID;

public class XmlParsers {
    //DOM parser
    public static Users domParseUserXml(String xml) throws ParserConfigurationException, IOException, SAXException {
        Users users = new Users();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        // This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all
        // XML entity attacks are prevented. Disable Future when exploit XXE
//        dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

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
        return users;
    }


    public static final String xxe = "src/main/resources/xml/xxe_exp.xml";


    public static String SaxParseUserXmlToString(String xml) throws IOException, DocumentException, SAXException {

        SAXReader reader = new SAXReader();
//        reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
//        reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
//        reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

        org.dom4j.Document document = reader.read(new InputSource(new StringReader(xml)));
        return document.asXML();
    }


/*

    public static List<User> staxParseUserXml(String xmlPath) {
        List<User> users = new ArrayList<>();
        User user = null;
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        // disable external entities. Disable Future when exploit XXE
        xmlInputFactory.setProperty("javax.xml.stream.isSupportingExternalEntities", false);
        try {
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(xmlPath));
            while (xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();

                if (xmlEvent.isStartElement()) {
                    StartElement startElement = xmlEvent.asStartElement();
                    if (startElement.getName().getLocalPart().equalsIgnoreCase("user")) {
                        user = new User();
                        Attribute idAttr = startElement.getAttributeByName(new QName("id"));
                        if (idAttr != null)
                            user.setUserId(Integer.parseInt(idAttr.getValue()));
                    }
                    //set other elements
                    else if (startElement.getName().getLocalPart().equalsIgnoreCase("username")) {
                        xmlEvent = xmlEventReader.nextEvent();
                        user.setUsername(xmlEvent.asCharacters().getData());
                    } else if (startElement.getName().getLocalPart().equalsIgnoreCase("password")) {
                        xmlEvent = xmlEventReader.nextEvent();
                        user.setPassword(xmlEvent.asCharacters().getData());
                    } else if (startElement.getName().getLocalPart().equalsIgnoreCase("group")) {
                        xmlEvent = xmlEventReader.nextEvent();
                        user.setGroup(xmlEvent.asCharacters().getData());
                    } else if (startElement.getName().getLocalPart().equalsIgnoreCase("email")) {
                        xmlEvent = xmlEventReader.nextEvent();
                        user.setEmail(xmlEvent.asCharacters().getData());
                    }
                }

                //if User end element is reached, add User object to list
                if (xmlEvent.isEndElement()) {
                    EndElement endElement = xmlEvent.asEndElement();
                    if (endElement.getName().getLocalPart().equalsIgnoreCase("user")) {
                        users.add(user);
                    }
                }
            }
        } catch (FileNotFoundException | XMLStreamException e) {
            e.printStackTrace();
        }
        return users;
    }


    public static List<User> saxParseUserXml(String xmlPath) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        List<User> users = null;
        try {
            // disable external entities. Disable Future when exploit XXE
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            SAXParser saxParser = factory.newSAXParser();
            MyHandler handler = new MyHandler();
            saxParser.parse(new File(xmlPath), handler);
            users = handler.getUsers();
        } catch (SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static class MyHandler extends DefaultHandler {
        public List<User> users = null;
        public User user = null;
        public StringBuilder data = null;

        public List<User> getUsers() {
            return users;
        }

        boolean bUsername = false;
        boolean bPassword = false;
        boolean bGroup = false;
        boolean bEmail = false;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equalsIgnoreCase("user")) {
                //create new User and put it in
                String id = attributes.getValue("id");
                user = new User();
                user.setUserId(Integer.parseInt(id));
                if (users == null)
                    users = new ArrayList<>();
            } else if (qName.equalsIgnoreCase("username"))
                bUsername = true;
            else if (qName.equalsIgnoreCase("password"))
                bPassword = true;
            else if (qName.equalsIgnoreCase("group"))
                bGroup = true;
            else if (qName.equalsIgnoreCase("email"))
                bEmail = true;
            data = new StringBuilder();
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (bUsername) {
                user.setUsername(data.toString());
                bUsername = false;
            } else if (bPassword) {
                user.setPassword(data.toString());
                bPassword = false;
            } else if (bGroup) {
                user.setGroup(data.toString());
                bGroup = false;
            } else if (bEmail) {
                user.setEmail(data.toString());
                bEmail = false;
            }
            if (qName.equalsIgnoreCase("User"))
                users.add(user);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            data.append(new String(ch, start, length));
        }
    }

*/

}

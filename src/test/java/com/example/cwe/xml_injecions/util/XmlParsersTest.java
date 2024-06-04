package com.example.cwe.xml_injecions.util;

import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.testng.annotations.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.testng.Assert.*;

public class XmlParsersTest {
    public static final String xxe = "src/main/resources/xml/xxe_exp1.xml";

    @Test
    public void testDomParse() throws ParserConfigurationException, SAXException, IOException {
        System.out.println(XmlParsers.domParseUserXml(String.join("\n", Files.readAllLines(Path.of(xxe)))));
    }
    @Test
    public static String SaxParseUserXmlToString() throws IOException, DocumentException, SAXException {

        //String body = WebUtils.getRequestBody(request);

        SAXReader reader = new SAXReader();
        reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
        reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

        org.dom4j.Document document = reader.read(new InputSource(new StringReader(String.join("\n", Files.readAllLines(Path.of(xxe)))))); // cause xxe

        return document.asXML();
    }

}
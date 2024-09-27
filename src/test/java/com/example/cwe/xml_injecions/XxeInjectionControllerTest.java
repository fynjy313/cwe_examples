package com.example.cwe.xml_injecions;

import com.example.cwe.xml_injecions.pojo.User;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import java.io.File;
import java.nio.file.Path;

public class XxeInjectionControllerTest {
    public static final String xxe = "src/main/resources/xml/xxe_exp.xml";
    public static final String xxe1 = "src/main/resources/xml/xxe_exp1.xml";

    /**
     * javax.xml.bind.UnmarshalException - with linked exception: [org.xml.sax.SAXParseException]
     * External Entity: Failed to read external document 'secrets.txt', because 'file' access is not allowed due to
     * restriction set by the accessExternalDTD property.
     * По умолчанию поддержка file, http в XXE отключена. Нужно System.setProperty("javax.xml.accessExternalDTD", "all");
     */
    @Test
    void xxeFileUnmarshall() throws JAXBException {

//        System.out.println(System.getProperty("javax.xml.accessExternalDTD"));
        System.setProperty("javax.xml.accessExternalDTD", "all");
//        System.out.println(System.getProperty("javax.xml.accessExternalDTD"));
        JAXBContext context = JAXBContext.newInstance(User.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        //unmarshaller.setProperty(javax.xml.XMLConstants.ACCESS_EXTERNAL_DTD, "all");
        System.out.println((User) unmarshaller.unmarshal(new File(xxe)));
    }

    /**
     * Works fine with non system entities
     */
    @Test
    void xxeLolUnmarshall() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(User.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        //unmarshaller.setProperty(javax.xml.XMLConstants.ACCESS_EXTERNAL_DTD, "all");
        System.out.println((User) unmarshaller.unmarshal(new File(xxe1)));
    }

    @Test
    void qweqwe() {

        String baseDir = "C://temp//tmp";
        System.out.println(Path.of(baseDir).resolve("/temp"));
        System.out.println(Path.of(baseDir).resolve("/java_projects"));
        System.out.println(Path.of(baseDir).resolve("tmp/../temp"));

        String fileGUID = "/../123";
        //result path will be "C:\temp\tmp\..\123.txt"
        System.out.println(new File("C://temp" + "/tmp" + fileGUID + ".txt"));
    }


}
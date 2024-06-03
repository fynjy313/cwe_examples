package com.example.cwe.xml_injecions;

import com.example.cwe.xml_injecions.dto.User;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import java.io.File;

import static org.testng.Assert.*;

public class XxeInjectionControllerTest {
    public static final String xxe1 = "src/main/resources/xml/xxe_exp.xml";

    @Test
    public void test1() throws JAXBException {
//        System.out.println(System.getProperty("javax.xml.accessExternalDTD"));
//        System.setProperty("javax.xml.accessExternalDTD", "all");
//        System.out.println(System.getProperty("javax.xml.accessExternalDTD"));

        JAXBContext context = JAXBContext.newInstance(User.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        //unmarshaller.setProperty(javax.xml.XMLConstants.ACCESS_EXTERNAL_DTD, "all");

        User user = (User) unmarshaller.unmarshal(new File(xxe1));
        System.out.println(user);
    }

}
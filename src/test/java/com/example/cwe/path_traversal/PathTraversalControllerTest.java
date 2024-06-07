package com.example.cwe.path_traversal;

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class PathTraversalControllerTest {


    @Test
    public void downloadResourceTest() throws IOException {
        String file = new String(getClass().getClassLoader()
                .getResourceAsStream("xml/lol.xml")
//                .getResourceAsStream("c:/temp/jpeg.jpeg")
                .readAllBytes(), StandardCharsets.UTF_8);

        System.out.println(file);

    }

}
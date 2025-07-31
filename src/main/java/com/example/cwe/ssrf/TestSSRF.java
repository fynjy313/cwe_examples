package com.example.cwe.ssrf;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

//TODO Протестировать возможность SSRF, добавить в ридми пример
public class TestSSRF {
    final static String pisUrl = "http://httpbin.org";

    private static URI getUri(String fileName, int fileSize) {
        UriComponents uriComponents = UriComponentsBuilder
                .fromHttpUrl(pisUrl)
                .queryParam("file", fileName)
                .queryParam("size", fileSize)
                .build();
        return uriComponents.toUri();

    }

    public static void main(String[] args) {
        String ssrf = ";@httpforever.com";

        System.out.println(getUri(ssrf,123));
    }


}


package com.example.cwe;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class SsrfControllerTest {
    @Test
    public void getURI() throws URISyntaxException, MalformedURLException {
        System.out.println(new URI("file://G:/work/new_ptai_policy.json"));
        System.out.println(new URI("file://G:/work/new_ptai_policy.json").toURL());
        URL url = new URI("http://httpforever.com:8080").toURL();
        System.out.println(url.getHost());
        System.out.println(url.getProtocol());
        System.out.println(url.getPort());

        String input = ";@evil.com";
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http").host("www.yourdomain.com").path(input).build(); // Safe!

        String site = "http://example.org";
        URL url1 = new URI(site + input).toURL(); // UnSafe!
        System.out.println("host: " + url.getHost());

        System.out.println(uriComponents);
        System.out.println(uriComponents.toUri());
        System.out.println(uriComponents.toUri().toURL());

    }


    @Test
    public void openStream() throws IOException {

        URL obj = new URL("file://G:/work/new_ptai_policy.json");
        BufferedReader in = new BufferedReader(new InputStreamReader(obj.openStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println((response));
    }

    @Test
    public void openStream2() throws IOException {
        URL obj = new URL("file://G:/work/new_ptai_policy.json");
        BufferedReader in = new BufferedReader(new InputStreamReader(obj.openStream()));

    }


}

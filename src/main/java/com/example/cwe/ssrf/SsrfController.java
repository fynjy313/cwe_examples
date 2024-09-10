package com.example.cwe.ssrf;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "ssrf")
public class SsrfController {

    @GetMapping("open-stream-unsafe")
    // test on http://httpforever.com or http://example.com to prevent SSL exceptions
    public String openStreamToRemoteObjectUnsafe(@RequestParam String location) throws Exception {
        URL url = new URI(location).toURL();
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        return reader.lines().collect(Collectors.joining());
    }

    @GetMapping("open-stream-safe")
    // test on http://httpforever.com or http://example.com to prevent SSL exceptions
    public String openStreamToRemoteObjectSafe(@RequestParam String location) throws Exception {
        URL url = new URI(location).toURL();

        if (!url.getHost().equals("example.com") ||
                !url.getProtocol().equals("http") && !url.getProtocol().equals("https")) {
            throw new Exception("Forbidden remote source");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        return reader.lines().collect(Collectors.joining());
    }


    @GetMapping("open-page-unsafe")
    public String openPageUnsafe(@RequestParam String location) throws URISyntaxException, IOException {
        /*Согласно RFC 3986 (https://www.rfc-editor.org/info/rfc3986)
        символы ';' и '@' являются зарезервированными символами, относящимися к sub-delims и gen-delims соответственно,
        и могут (или не могут) быть определены в качестве разделителей (зависит от реализации)*/

        // ssrf/open-page-unsafe?location=/anything - OK
        // ssrf/open-page-unsafe?location=;@httpforever.com - SSRF
        // ssrf/open-page-unsafe?location=;@httpbin.org/image/jpeg - SSRF

        URL url = new URI("http://httpbin.org" + location).toURL();
        System.out.printf("url: %s\nhost: %s\n", url, url.getHost());
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

        return reader.lines().collect(Collectors.joining());
    }

    @GetMapping("open-page-safe")
    public String openPageSafe(@RequestParam String location) throws IOException {
        // Test request: /ssrf/open-page-safe?location=;@httpforever.com
        //TODO: Qodana tells that there is a Open redirect vuln here - CHECK! if true - do example
        URL resultUrl = UriComponentsBuilder.newInstance()
                .scheme("http").host("httpbin.org").path("anything/").path(location).build().toUri().toURL();
        System.out.printf("url: %s\nhost: %s\n", resultUrl, resultUrl.getHost());
        BufferedReader reader = new BufferedReader(new InputStreamReader(resultUrl.openStream()));

        return reader.lines().collect(Collectors.joining());
    }

    @GetMapping("download-file-unsafe")
    public void downloadFileUnsafe(@RequestParam String location, HttpServletResponse response) throws IOException, URISyntaxException {
        // remote image: /ssrf/download-file?location=http://httpbin.org/image/jpeg
        // SSRF exp: location=file:///G:/work/new_ptai_policy.json
        // SSRF exp: location=file:///etc/passwd
        URL url = new URI(location).toURL();
        response.setHeader("content-disposition", "attachment;fileName=" + url.getFile());
        int length;
        byte[] bytes = new byte[1024];
        InputStream inputStream = url.openStream(); // send request
        OutputStream outputStream = response.getOutputStream();
        while ((length = inputStream.read(bytes)) > 0) {
            outputStream.write(bytes, 0, length);
        }
//        url.openStream().transferTo(response.getOutputStream()); // short way
    }

    @GetMapping("download-file-safe")
    public void downloadFileSafe(@RequestParam String location, HttpServletResponse response) throws Exception {
        // Проверяем протокол, запрещаем локальные адреса и отключаем редиректы.
        // Не рекомендуется использовать, т.к. это по сути вариант черного списка
        // Данный метод не защитит от атак типа TOCTOU (Time of Check to Time of Use)
        URL url = new URI(location).toURL();
        InetAddress inetAddress = InetAddress.getByName(url.getHost());

        if (!url.getProtocol().startsWith("http")) {
            // Возвращать клиенту ошибку нельзя, это только для примера
            response.getWriter().write("Wrong protocol: " + url.getProtocol());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            throw new Exception("Forbidden remote source");

        } else if (inetAddress.isAnyLocalAddress() || inetAddress.isLoopbackAddress() || inetAddress.isLinkLocalAddress()) {
            response.getWriter().write("Wrong ip address: " + inetAddress);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        } else {// All checks OK. Processing...
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            conn.getInputStream().transferTo(response.getOutputStream());
        }
    }


    @GetMapping("hello")
    public String hello() {
        return "Hello!";
    }

}

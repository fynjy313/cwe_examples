package com.example.cwe.ssrf;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Arrays;
import java.util.List;


@WebServlet("/ssrf-servlet")
public class SsrfServletController extends HttpServlet {


    private static final List<String> VALID_URI = Arrays.asList("https://qwe.rty", "https://asd.fgh");
    private HttpClient client = HttpClient.newHttpClient();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            URI uri = new URI(request.getParameter("uri"));

            // BAD: a request parameter is incorporated without validation into a HTTP request
            HttpRequest r = HttpRequest.newBuilder(uri).build();
            client.send(r, null);

            // GOOD: the request parameter is validated against a known fixed list
            if (VALID_URI.contains(request.getParameter("uri"))) {
                HttpRequest r2 = HttpRequest.newBuilder(uri).build();
                client.send(r2, null);
            }
        } catch (URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

/*    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter printWriter = resp.getWriter();
        printWriter.write("Hello!");
        printWriter.close();
    }*/
}

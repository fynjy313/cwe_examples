package com.example.cwe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CweExamplesApplication {
    //TODO: раскрытие логов:
//	https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.3-Release-Notes#changes-to-the-default-error-pages-content
//	https://dev.to/abdelrani/error-handling-in-spring-web-using-rfc-9457-specification-5dj1
    //TODO: SOP, CORS exp?

    public static void main(String[] args) {
        SpringApplication.run(CweExamplesApplication.class, args);
    }

}

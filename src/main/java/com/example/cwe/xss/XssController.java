package com.example.cwe.xss;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/xss")
public class XssController {

    //TODO: beautify - JS ?


    @GetMapping("/html")
    public String jspExp() {
        return "xss_page";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/feedback")
    public String feedBack() {
        return "feedback";
    }


}

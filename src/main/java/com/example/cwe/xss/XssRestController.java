package com.example.cwe.xss;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/xss")
public class XssRestController {
    private List<String> words = new ArrayList<>(List.of("Recorded words: "));

    @PostMapping("/save-feedback")
    public void storedXssExample1(@RequestParam String feedBack) {
        words.add(feedBack);
    }

    @GetMapping("/show-feedback")
    public String showFeedBack() {
        return words.toString();
    }

    @GetMapping("/exp")
    public String simpleXxsExample() {
        return "<script>alert('XSS')</script>";
    }

    /**
     * Reflected XSS.
     * Try feedBack=<script>alert(%27reflected%20XSS%27)</script>
     *
     * @param feedBack Name to greeting
     * @return 'Hello' + feedBack
     */
    @GetMapping("/reflected1")
    public String reflectedXssExample1(@RequestParam String feedBack) {
        words.add(feedBack);
        return "Hello, " + feedBack + "!";
    }
}

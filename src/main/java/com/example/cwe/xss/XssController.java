package com.example.cwe.xss;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("xss")
public class XssController {
    private List<String> words = new ArrayList<>(List.of("Recorded words: "));

    @GetMapping("/")
    public String storedXssExample1() {
        return words.toString();
    }

    @GetMapping("/exp")
    public String simpleXxsExample() {
        return "<script>alert('XSS')</script>";
    }

    /**
     * Reflected XSS.
     * Try name=<script>alert(%27reflected%20XSS%27)</script>
     *
     * @param name Name to greeting
     * @return 'Hello' + name
     */
    @GetMapping("reflected1")
    public String reflectedXssExample1(@RequestParam String name) {
        words.add(name);
        return "Hello, " + name + "!";
    }
}

package com.example.cwe.xss;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
//@Controller
@RequestMapping("/xss")
public class ReflectedXssController {


    @GetMapping("/exp")
    public String simpleXxsExample() {
        return "<script>alert('XSS')</script>";
    }

    /**
     * Reflected XSS.
     * Try: ?name=<script>alert(%27reflected%20XSS%27)</script>
     *
     * @param name Name to greeting
     * @return 'Hello' + name
     */
    @GetMapping("/reflected")
    public String reflectedXssExample1(@RequestParam String name) {
        return "Hello, " + name + "!";
    }

//    @GetMapping("/reflected2")
//    public String reflectedXssExample2() {
//        return "greet";
//    }
}

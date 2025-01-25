package com.example.cwe.xss;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/xss")
public class StoredXssController {
    //TODO: feedback page on JSP
    //TODO: login + session + cookie
    //TODO: xqj-api-1.0.jar - remove or move to dir - find usages?

    private final List<FeedBackObj> feedBackObjs = new ArrayList<>();


    @GetMapping("/")
    public String feedBack(Model model) {
        model.addAttribute("feedBacks", feedBackObjs);
        return "xss-page";
    }

    /**
     * Stored XSS.
     * Try post feedBack like '<script>alert('XSS')</script>'
     * This string will be recorded and displayed at page xss/feedback
     *
     * @param feedBack Some feedback
     */
    @PostMapping("/save-feedback")
    public ResponseEntity<?> storedXssExample1(@RequestParam String feedBack) {
        feedBackObjs.add(new FeedBackObj(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), feedBack));
        return ResponseEntity.ok("FeedBack successfully added");
    }

    @RequestMapping("/qwe")
    public String qwe(){
        return "QWE!";
    }


    @Data
    @AllArgsConstructor
    static class FeedBackObj {
        private String date;
        private String text;
    }


}

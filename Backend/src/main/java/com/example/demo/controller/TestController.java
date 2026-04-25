package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class TestController {

    @GetMapping("/hello")
    public String helloAdmin() {
        return "Hello Admin!";
    }
}

package com.bednarskibrzozowski.database.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

    @GetMapping(path = "/open")
    public String open() {
        return "No login required!";
    }

    @GetMapping
    public String closed() {
        return "Login is required!";
    }

}

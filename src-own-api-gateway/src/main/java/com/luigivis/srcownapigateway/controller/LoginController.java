package com.luigivis.srcownapigateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/login")
public class LoginController {

    @GetMapping("")
    public String test() {
        return """
                <h1>Hello World</h1>
                <h2>Bymatt Gay</h2>
                """;
    }
}

package com.gamelink.gamelinkapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @GetMapping("/demo")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello, World!");
    }
}

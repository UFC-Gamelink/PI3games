package com.gamelink.gamelinkapi.controllers;

import com.gamelink.gamelinkapi.services.firebase.FirebaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DemoController {
    private final FirebaseService firebaseService;
    @GetMapping("/demo")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello, World!");
    }
    @PostMapping("/firebase")
    public ResponseEntity teste() {
        return ResponseEntity.ok().body(firebaseService.saveImage());
    }
}

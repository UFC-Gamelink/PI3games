package com.gamelink.gamelinkapi.controllers;

import com.gamelink.gamelinkapi.dtos.requests.users.UserProfileRequest;
import com.gamelink.gamelinkapi.services.users.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService service;
    @PostMapping
    public ResponseEntity<Void> post(@RequestBody @Valid UserProfileRequest userProfileRequest) {
        service.save(userProfileRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}

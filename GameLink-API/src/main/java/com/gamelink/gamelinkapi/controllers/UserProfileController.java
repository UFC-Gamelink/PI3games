package com.gamelink.gamelinkapi.controllers;

import com.gamelink.gamelinkapi.dtos.requests.users.UserProfileRequest;
import com.gamelink.gamelinkapi.services.users.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserProfileController implements ICrudController<UserProfileRequest> {
    private final UserProfileService service;
    @PostMapping
    @Override
    public ResponseEntity<Void> post(@RequestBody @Valid UserProfileRequest userProfileRequest) {
        service.save(userProfileRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity
                .accepted()
                .build();
    }

    @Override
    public ResponseEntity<UserProfileRequest> getById(UUID id) {
        return null;
    }
}

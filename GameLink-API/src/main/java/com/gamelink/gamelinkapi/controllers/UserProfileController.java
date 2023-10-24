package com.gamelink.gamelinkapi.controllers;

import com.gamelink.gamelinkapi.dtos.requests.users.PostUserProfileRequest;
import com.gamelink.gamelinkapi.dtos.requests.users.PutUserProfileRequest;
import com.gamelink.gamelinkapi.dtos.responses.users.UserProfileResponse;
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
public class UserProfileController implements ICrudController<PostUserProfileRequest> {
    private final UserProfileService service;
    @PostMapping
    @Override
    public ResponseEntity<Void> post(@RequestBody @Valid PostUserProfileRequest postUserProfileRequest) {
        service.save(postUserProfileRequest);
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

    @GetMapping
    public ResponseEntity<UserProfileResponse> get() {
        UserProfileResponse userProfile = service.findUserProfile();
        return ResponseEntity
                .ok()
                .body(userProfile);
    }

    @PutMapping
    public ResponseEntity<UserProfileResponse> put(@RequestBody @Valid PutUserProfileRequest userProfile) {
        UserProfileResponse response = service.updateProfile(userProfile);
        return ResponseEntity
                .ok()
                .body(response);
    }
}

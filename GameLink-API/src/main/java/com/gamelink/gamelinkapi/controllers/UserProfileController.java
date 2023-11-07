package com.gamelink.gamelinkapi.controllers;

import com.gamelink.gamelinkapi.dtos.requests.users.PostUserProfileRequest;
import com.gamelink.gamelinkapi.dtos.requests.users.PutUserProfileRequest;
import com.gamelink.gamelinkapi.dtos.responses.users.UserProfileResponse;
import com.gamelink.gamelinkapi.services.users.UserProfileService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/images")
    public ResponseEntity<UserProfileResponse> postIconAndBanner(
            @RequestPart @NotNull MultipartFile icon,
            @RequestPart @NotNull MultipartFile banner
    ) {
        UserProfileResponse response = service.saveImages(icon, banner);
        return ResponseEntity
                .accepted()
                .body(response);
    }

    @PutMapping("/images")
    public ResponseEntity<UserProfileResponse> putIconAndBanner(
            @RequestPart @NotNull MultipartFile icon,
            @RequestPart @NotNull MultipartFile banner
    ) {
        UserProfileResponse response = service.updateImages(icon, banner);
        return ResponseEntity
                .accepted()
                .body(response);
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

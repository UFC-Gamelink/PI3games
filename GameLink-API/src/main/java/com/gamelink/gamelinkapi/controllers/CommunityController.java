package com.gamelink.gamelinkapi.controllers;

import com.gamelink.gamelinkapi.dtos.requests.communities.CommunityRequest;
import com.gamelink.gamelinkapi.dtos.responses.communities.CommunityResponse;
import com.gamelink.gamelinkapi.services.communities.CommunityService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/communities")
public class CommunityController {
    private final CommunityService communityService;

    @PostMapping
    public ResponseEntity<Void> post(@RequestBody @Valid CommunityRequest communityRequest) {
        communityService.createCommunity(communityRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/banner/{id}")
    public ResponseEntity<CommunityResponse> postBanner(@PathVariable UUID id, @RequestPart @NotNull MultipartFile banner) {
        CommunityResponse communityUpdated = communityService.createBanner(id, banner);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(communityUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        communityService.deleteCommunity(id);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .build();
    }

    @GetMapping
    public ResponseEntity<List<CommunityResponse>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(communityService.getCommunities());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommunityResponse> update(@PathVariable UUID id, @RequestBody @Valid CommunityRequest communityRequest) {
        CommunityResponse communityResponse = communityService.updateCommunity(id, communityRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(communityResponse);
    }

    @PutMapping("/banner/{id}")
    public ResponseEntity<CommunityResponse> updateBanner(@PathVariable UUID id, @RequestPart @NotNull MultipartFile banner) {
        CommunityResponse communityResponse = communityService.updateBanner(id, banner);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(communityResponse);
    }
}

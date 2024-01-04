package com.gamelink.gamelinkapi.controllers;

import com.gamelink.gamelinkapi.dtos.requests.posts.EventPostRequest;
import com.gamelink.gamelinkapi.dtos.requests.posts.PostRequest;
import com.gamelink.gamelinkapi.dtos.responses.posts.PostResponse;
import com.gamelink.gamelinkapi.services.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/image")
    public ResponseEntity<PostResponse> post(
            @RequestPart MultipartFile image,
            @RequestPart @NotBlank String description
    ) {
        postService.save(image, description);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping
    public ResponseEntity<PostResponse> post(
            @RequestPart @NotBlank String description
    ) {
        postService.save(null, description);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/event")
    public ResponseEntity<PostResponse> post(@RequestBody @Valid EventPostRequest eventPostRequest) {
        postService.saveEventPost(eventPostRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PutMapping("/{id}/like")
    public ResponseEntity<Boolean> likePost(@PathVariable UUID id) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(postService.changeLike(id));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> get() {
        return ResponseEntity
                .ok()
                .body(postService.findAll());
    }

    @GetMapping("/recommended")
    public ResponseEntity<List<PostResponse>> getRecommended() {
        return ResponseEntity
                .ok()
                .body(postService.findRecommended());
    }

    @GetMapping("/communities")
    public ResponseEntity<List<PostResponse>> getAllCommunitiesPosts() {
        List<PostResponse> communitiesPosts = postService.findCommunitiesPosts();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(communitiesPosts);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        postService.delete(id);
        return ResponseEntity
                .accepted()
                .build();
    }

    @PostMapping("/community/{id}/image")
    public ResponseEntity<Void> addPost(
            @PathVariable UUID id,
            @RequestPart @NotBlank String description,
            @RequestPart MultipartFile image
    ) {
        PostRequest postRequest = new PostRequest(description, image);
        postService.saveCommunityPost(id, postRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/community/{id}")
    public ResponseEntity<Void> addPost(
            @PathVariable UUID id,
            @RequestPart @NotBlank String description
    ) {
        PostRequest postRequest = new PostRequest(description, null);
        postService.saveCommunityPost(id, postRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}

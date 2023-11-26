package com.gamelink.gamelinkapi.controllers;

import com.gamelink.gamelinkapi.dtos.responses.posts.PostResponse;
import com.gamelink.gamelinkapi.services.posts.PostService;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        postService.delete(id);
        return ResponseEntity
                .accepted()
                .build();
    }
}

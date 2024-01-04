package com.gamelink.gamelinkapi.controllers;

import com.gamelink.gamelinkapi.dtos.requests.commentaries.CommentaryRequest;
import com.gamelink.gamelinkapi.dtos.responses.commentaries.CommentaryResponse;
import com.gamelink.gamelinkapi.services.CommentaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/commentaries")
public class CommentaryController {
    private final CommentaryService commentaryService;

    @PostMapping
    public ResponseEntity<Void> post(
            @RequestParam("postId") UUID postId,
            @RequestBody @Valid CommentaryRequest commentaryRequest
    ) {
        commentaryService.save(commentaryRequest, postId);
        return  ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping
    public ResponseEntity<List<CommentaryResponse>> get(@RequestParam("postId") UUID postId) {
        var commentariesByPostId = commentaryService.findCommentariesByPostId(postId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentariesByPostId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        commentaryService.delete(id);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .build();
    }
}

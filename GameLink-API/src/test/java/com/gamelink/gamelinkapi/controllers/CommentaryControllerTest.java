package com.gamelink.gamelinkapi.controllers;

import com.gamelink.gamelinkapi.dtos.requests.commentaries.CommentaryRequest;
import com.gamelink.gamelinkapi.dtos.responses.commentaries.CommentaryResponse;
import com.gamelink.gamelinkapi.services.CommentaryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class CommentaryControllerTest {
    @Autowired
    private CommentaryController controller;
    @MockBean
    private CommentaryService service;

    @Test
    @DisplayName("post should execute save from CommentaryService and return a created status when success")
    void postShouldReturnACreatedStatusWhenSuccess() {
        final var postId = UUID.randomUUID();
        final var commentary = new CommentaryRequest("Text");

        ResponseEntity<Void> response = controller.post(postId, commentary);

        verify(service, times(1)).save(commentary, postId);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("Delete should execute delete from CommentaryService and return an Accepted status when success")
    void deleteShouldExecuteDeleteFromCommentaryServiceWhenSuccess() {
        UUID validId = UUID.randomUUID();

        ResponseEntity<Void> response = controller.delete(validId);

        verify(service, times(1)).delete(validId);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    @DisplayName("get should execute findCommentariesByPostId from CommentaryService and return a valid CommentaryResponse status when success")
    void getShouldExecuteFindUserProfileFromUserProfileServiceWhenSuccess() {
        final var postId = UUID.randomUUID();
        final var commentaryResponse = new CommentaryResponse(UUID.randomUUID(), "commentary", postId, UUID.randomUUID(), "owner", "username", "url");
        when(service.findCommentariesByPostId(postId)).thenReturn(List.of(commentaryResponse));

        ResponseEntity<List<CommentaryResponse>> response = controller.get(postId);

        verify(service, times(1)).findCommentariesByPostId(postId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(commentaryResponse, response.getBody().get(0));
    }
}

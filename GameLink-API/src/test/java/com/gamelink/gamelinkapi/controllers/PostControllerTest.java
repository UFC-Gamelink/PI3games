package com.gamelink.gamelinkapi.controllers;

import com.gamelink.gamelinkapi.dtos.requests.posts.EventPostRequest;
import com.gamelink.gamelinkapi.dtos.requests.posts.PostRequest;
import com.gamelink.gamelinkapi.dtos.responses.posts.PostResponse;
import com.gamelink.gamelinkapi.services.PostService;
import com.gamelink.gamelinkapi.utils.creators.PostCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PostControllerTest {
    @Autowired
    private PostController controller;
    @MockBean
    private PostService service;

    private final PostCreator postCreator = PostCreator.getInstance();

    @Test
    @DisplayName("post should execute save from PostService and return a created status when success")
    void postShouldReturnACreatedStatusWhenSuccess() {
        final var postText = "post text";

        ResponseEntity<PostResponse> response = controller.post(postText);

        verify(service, times(1)).save(null, postText);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("Delete should execute delete from PostService and return an Accepted status when success")
    void deleteShouldExecuteDeleteFromUserProfileServiceWhenSuccess() {
        UUID validId = UUID.randomUUID();

        ResponseEntity<Void> response = controller.delete(validId);

        verify(service, times(1)).delete(validId);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    @DisplayName("get should execute findAll from PostService and return a valid PostResponse status when success")
    void getShouldExecuteFindUserProfileFromUserProfileServiceWhenSuccess() {
        var postResponse = postCreator.createValid();
        when(service.findAll()).thenReturn(List.of(postResponse));

        ResponseEntity<List<PostResponse>> response = controller.get();

        verify(service, times(1)).findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(postResponse, response.getBody().get(0));
    }

    @Test
    @DisplayName("likePost Should return true when the post is not liked yet and execute changeLike in PostService")
    void likePostShouldReturnTrueWhenSuccess() {
        UUID postId = UUID.randomUUID();

        when(service.changeLike(postId)).thenReturn(true);

        ResponseEntity<Boolean> response = controller.likePost(postId);

        verify(service, times(1)).changeLike(postId);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody());
    }

    @DisplayName("addPost with only text post should execute saveCommunityPost in PostService and return a success status when success")
    void addPostWithOnlyTextSuccess(){
        final UUID communityId = UUID.randomUUID();
        final String postText = "post";

        ResponseEntity<Void> response = controller.addPost(communityId, postText);

        verify(service, times(1))
                .saveCommunityPost(communityId, new PostRequest(postText, null));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("addPost post should execute addPost in CommunityService and return a success status when success")
    void addPostSuccess() {
        final UUID communityId = UUID.randomUUID();
        final String postText = "post";

        ResponseEntity<Void> response = controller.addPost(communityId, postText, null);

        verify(service, times(1))
                .saveCommunityPost(communityId, new PostRequest(postText, null));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Post with image should  execute save in post Service and return a Created status when success")
    void postImageSuccess() {
        final var postText = "post text";
        final var image = Mockito.mock(MultipartFile.class);

        ResponseEntity<PostResponse> response = controller.post(image, postText);

        verify(service, times(1)).save(image, postText);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("Post event should  execute saveEventPost in post Service and return a Created status when success")
    void postEventSuccess() {
        final var postRequest = new EventPostRequest("text", 1.2, 1.2, LocalDateTime.now());

        ResponseEntity<PostResponse> response = controller.post(postRequest);

        verify(service, times(1)).saveEventPost(postRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}

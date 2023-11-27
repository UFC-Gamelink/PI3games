package com.gamelink.gamelinkapi.controllers;

import com.gamelink.gamelinkapi.dtos.requests.communities.CommunityRequest;
import com.gamelink.gamelinkapi.dtos.requests.posts.PostRequest;
import com.gamelink.gamelinkapi.dtos.responses.communities.CommunitiesGeneralResponse;
import com.gamelink.gamelinkapi.services.communities.CommunityService;
import com.gamelink.gamelinkapi.utils.creators.CommunityRequestCreator;
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
public class CommunityControllerTest {
    @Autowired
    private CommunityController controller;
    @MockBean
    private CommunityService service;
    private final CommunityRequestCreator creator  = CommunityRequestCreator.getInstance();

    @Test
    @DisplayName("post should execute createCommunity from PostService and return a created status when success")
    void postShouldReturnACreatedStatusWhenSuccess() {
        final var communityRequest = new CommunityRequest("name", "description");

        ResponseEntity<Void> response = controller.post(communityRequest);

        verify(service, times(1)).createCommunity(communityRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("Delete should execute deleteCommunity from CommunityService and return an Accepted status when success")
    void deleteShouldExecuteDeleteFromUserProfileServiceWhenSuccess() {
        UUID validId = UUID.randomUUID();

        ResponseEntity<Void> response = controller.delete(validId);

        verify(service, times(1)).deleteCommunity(validId);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    @DisplayName("get should execute getCommunities from CommunityService and return a valid CommunityResponse status when success")
    void getShouldExecuteFindUserProfileFromUserProfileServiceWhenSuccess() {
        var postResponse = new CommunitiesGeneralResponse(UUID.randomUUID(), "community", "description", "url", "owner", UUID.randomUUID());
        when(service.getCommunities()).thenReturn(List.of(postResponse));

        ResponseEntity<List<CommunitiesGeneralResponse>> response = controller.getAll();

        verify(service, times(1)).getCommunities();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(postResponse, response.getBody().get(0));
    }

    @Test
    @DisplayName("enterCommunity should execute enterCommunity in CommunityService and return a success status when success")
    void enterCommunitySuccess(){
        UUID communityId = UUID.randomUUID();

        ResponseEntity<Void> response = controller.enterCommunity(communityId);

        verify(service, times(1)).enterCommunity(communityId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("exitCommunity should execute exitCommunity in CommunityService and return a success status when success")
    void exitCommunitySuccess(){
        UUID communityId = UUID.randomUUID();

        ResponseEntity<Void> response = controller.exitCommunity(communityId);

        verify(service, times(1)).exitCommunity(communityId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("addPost with only text post should execute addPost in CommunityService and return a success status when success")
    void addPostWithOnlyTextSuccess(){
        final UUID communityId = UUID.randomUUID();
        final String postText = "post";

        ResponseEntity<Void> response = controller.addPost(communityId, postText);

        verify(service, times(1))
                .addPost(communityId, new PostRequest(postText, null));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("addPost post should execute addPost in CommunityService and return a success status when success")
    void addPostSuccess(){
        final UUID communityId = UUID.randomUUID();
        final String postText = "post";

        ResponseEntity<Void> response = controller.addPost(communityId, postText, null);

        verify(service, times(1))
                .addPost(communityId, new PostRequest(postText, null));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}

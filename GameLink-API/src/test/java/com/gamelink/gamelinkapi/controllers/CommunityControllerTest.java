package com.gamelink.gamelinkapi.controllers;

import com.gamelink.gamelinkapi.dtos.requests.communities.CommunityRequest;
import com.gamelink.gamelinkapi.dtos.responses.communities.CommunitiesGeneralResponse;
import com.gamelink.gamelinkapi.dtos.responses.communities.CommunityResponse;
import com.gamelink.gamelinkapi.dtos.responses.communities.PostCommunityResponse;
import com.gamelink.gamelinkapi.dtos.responses.posts.PostResponse;
import com.gamelink.gamelinkapi.services.CommunityService;
import com.gamelink.gamelinkapi.utils.creators.CommunityRequestCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class CommunityControllerTest {
    @Autowired
    private CommunityController controller;
    @MockBean
    private CommunityService service;
    private final CommunityRequestCreator creator  = CommunityRequestCreator.getInstance();

    @Test
    @DisplayName("post should execute createCommunity from PostService and return a created status when success")
    void postShouldReturnACreatedStatusWhenSuccess() {
        final var communityRequest = new CommunityRequest("name", "description");

        ResponseEntity<PostCommunityResponse> response = controller.post(communityRequest);

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
        var postResponse = new CommunitiesGeneralResponse(UUID.randomUUID(), "community", "description", "url", "owner", UUID.randomUUID(), 0);
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
    @DisplayName("postBanner should execute createBanner in CommunityService and return a Created status when success")
    void postBannerSuccess() {
        var id = UUID.randomUUID();
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        var response = createCommunityResponse();

        when(service.createBanner(id, multipartFile)).thenReturn(response);

        ResponseEntity<CommunityResponse> responseReceived = controller.postBanner(id, multipartFile);

        verify(service, times(1)).createBanner(id, multipartFile);
        assertEquals(HttpStatus.CREATED, responseReceived.getStatusCode());
        assertNotNull(responseReceived);
        assertEquals(response, responseReceived.getBody());
    }


    @Test
    @DisplayName("GetById should execute getCommunity in CommunityService and return a Ok status when success")
    void getByIdSuccess() {
        var id = UUID.randomUUID();
        var validResponse = createCommunityResponse();
        when(service.getCommunity(id)).thenReturn(validResponse);

        ResponseEntity<CommunityResponse> response = controller.getById(id);

        verify(service, times(1)).getCommunity(id);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(validResponse, response.getBody());
    }

    @Test
    @DisplayName("getPostsById should execute getPostsById in CommunityService and return a Ok status when success")
    void getPostsByIdSuccess() {
        var id = UUID.randomUUID();
        when(service.getCommunityPosts(id)).thenReturn(List.of());

        ResponseEntity<List<PostResponse>> response = controller.getPostsById(id);

        verify(service, times(1)).getCommunityPosts(id);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    @DisplayName("GetMy should execute getMyCommunities in CommunityService and return a Ok status when success")
    void getMySuccess() {
        var validResponse = List.of(createCommunityGeneralResponse());
        when(service.getMyCommunities()).thenReturn(validResponse);

        ResponseEntity<List<CommunitiesGeneralResponse>> response = controller.getMy();

        verify(service, times(1)).getMyCommunities();
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(validResponse.get(0), response.getBody().get(0));
    }

    @Test
    @DisplayName("updateBanner should execute updateBanner in CommunityService and return a Ok status when success")
    void updateBannerSuccess() {
        var id = UUID.randomUUID();
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        var response = createCommunityResponse();

        when(service.updateBanner(id, multipartFile)).thenReturn(response);

        ResponseEntity<CommunityResponse> responseReceived = controller.updateBanner(id, multipartFile);

        verify(service, times(1)).updateBanner(id, multipartFile);
        assertEquals(HttpStatus.OK, responseReceived.getStatusCode());
        assertNotNull(responseReceived);
        assertEquals(response, responseReceived.getBody());
    }

    @Test
    @DisplayName("update should execute updateCommunity in CommunityService and return a Ok status when success")
    void updateSuccess() {
        var id = UUID.randomUUID();
        var response = createCommunityResponse();
        var request = creator.createValid();

        when(service.updateCommunity(id, request)).thenReturn(response);

        ResponseEntity<CommunityResponse> responseReceived = controller.update(id, request);

        verify(service, times(1)).updateCommunity(id, request);
        assertEquals(HttpStatus.OK, responseReceived.getStatusCode());
        assertNotNull(responseReceived);
        assertEquals(response, responseReceived.getBody());
    }

    private static CommunityResponse createCommunityResponse() {
        return new CommunityResponse(UUID.randomUUID(), "name", "description", "url", "owner", UUID.randomUUID(), List.of());
    }


    private static CommunitiesGeneralResponse createCommunityGeneralResponse() {
        return new CommunitiesGeneralResponse(UUID.randomUUID(), "name", "description", "url", "owner", UUID.randomUUID(), 0);
    }
}

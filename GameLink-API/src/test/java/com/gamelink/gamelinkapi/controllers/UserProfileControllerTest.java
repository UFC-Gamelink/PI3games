package com.gamelink.gamelinkapi.controllers;

import com.gamelink.gamelinkapi.dtos.requests.users.PostUserProfileRequest;
import com.gamelink.gamelinkapi.dtos.responses.users.UserProfileResponse;
import com.gamelink.gamelinkapi.services.users.UserProfileService;
import com.gamelink.gamelinkapi.utils.creators.UserProfileRequestCreator;
import com.gamelink.gamelinkapi.utils.creators.UserProfileResponseCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserProfileControllerTest {
    @Autowired
    private UserProfileController controller;
    @MockBean
    private UserProfileService service;
    private final UserProfileRequestCreator requestCreator = UserProfileRequestCreator.getInstance();
    private final UserProfileResponseCreator responseCreator = UserProfileResponseCreator.getInstance();

    @Test
    @DisplayName("post should execute save from UserProfileService and return a created status when success")
    void postShouldReturnACreatedStatusWhenSuccess() {
        PostUserProfileRequest validPostUserProfileRequest = requestCreator.createValid();

        ResponseEntity<Void> response = controller.post(validPostUserProfileRequest);

        verify(service, times(1)).save(validPostUserProfileRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("Delete should execute delete from userProfileService and return aAccepted status when success")
    void deleteShouldExecuteDeleteFromUserProfileServiceWhenSuccess(){
        UUID validId = UUID.randomUUID();

        ResponseEntity<Void> response = controller.delete(validId);

        verify(service, times(1)).delete(validId);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    @DisplayName("get should execute getUserProfile from userProfileService and return a valid UserProfileResponse status when success")
    void getShouldExecuteFindUserProfileFromUserProfileServiceWhenSuccess(){
        var userProfileResponse = responseCreator.createValid();
        when(service.findUserProfile()).thenReturn(userProfileResponse);
        ResponseEntity<UserProfileResponse> response = controller.get();

        verify(service, times(1)).findUserProfile();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userProfileResponse, response.getBody());
    }
}

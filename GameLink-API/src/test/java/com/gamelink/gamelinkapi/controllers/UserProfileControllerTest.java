package com.gamelink.gamelinkapi.controllers;

import com.gamelink.gamelinkapi.dtos.requests.users.UserProfileRequest;
import com.gamelink.gamelinkapi.mappers.UserProfileMapper;
import com.gamelink.gamelinkapi.services.users.UserProfileService;
import com.gamelink.gamelinkapi.utils.creators.UserProfileRequestCreator;
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
public class UserProfileControllerTest {
    @Autowired
    private UserProfileController controller;
    @MockBean
    private UserProfileService service;
    private final UserProfileRequestCreator creator = UserProfileRequestCreator.getInstance();
    private final UserProfileMapper mapper = UserProfileMapper.INSTANCE;

    @Test
    @DisplayName("post should execute save from UserProfileService and return a created status when success")
    void postShouldReturnACreatedStatusWhenSuccess() {
        UserProfileRequest validUserProfileRequest = creator.createValid();
        when(service.save(validUserProfileRequest))
                .thenReturn(mapper.requestToResponseDto(validUserProfileRequest));

        ResponseEntity<Void> response = controller.post(validUserProfileRequest);

        verify(service, times(1)).save(validUserProfileRequest);
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
}
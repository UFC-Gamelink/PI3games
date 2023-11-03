package com.gamelink.gamelinkapi.controllers;


import com.gamelink.gamelinkapi.dtos.requests.authentication.AuthenticationRequest;
import com.gamelink.gamelinkapi.dtos.requests.authentication.RegisterRequest;
import com.gamelink.gamelinkapi.dtos.responses.authentication.AuthenticationResponse;
import com.gamelink.gamelinkapi.services.users.AuthenticationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class AuthenticationControllerTest {
    @Autowired
    private AuthenticationController authenticationController;
    @MockBean
    private AuthenticationService authenticationService;

    @Test
    @DisplayName("Register method should execute register from AuthenticationService and return created status when is successful executed")
    void registerShouldExecuteWithSuccessWhenRequestHasAValidFormat() {
        final RegisterRequest validRegisterRequest = new RegisterRequest("username", "valid@email.com", "@Aa1abcd");

        final ResponseEntity<AuthenticationResponse> response = authenticationController.register(validRegisterRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(authenticationService, times(1)).register(validRegisterRequest);
    }

    @Test
    @DisplayName("Authenticate should execute autenticate from AuthenticationService and return a success status when is successful executed")
    void authenticateShouldExecuteWithSuccessWhenRequestHasAValidFormat() {
        final AuthenticationRequest validRegisterRequest = new AuthenticationRequest("username", "@Aa1abcd");

        final ResponseEntity<AuthenticationResponse> response = authenticationController.authenticate(validRegisterRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authenticationService, times(1)).authenticate(validRegisterRequest);
    }
}

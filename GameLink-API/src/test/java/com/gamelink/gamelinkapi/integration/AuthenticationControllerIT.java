package com.gamelink.gamelinkapi.integration;

import com.gamelink.gamelinkapi.dtos.requests.RegisterRequest;
import com.gamelink.gamelinkapi.dtos.responses.AuthenticationResponse;
import com.gamelink.gamelinkapi.exceptions.BadRequestExceptionDetails;
import com.gamelink.gamelinkapi.models.User;
import com.gamelink.gamelinkapi.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class AuthenticationControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Register method should return created status when is successful executed")
    void registerShouldExecuteWithSuccessWhenRequestHasAValidFormat() {
        final RegisterRequest validRegisterRequest = new RegisterRequest("valid@email.com", "Ae1!vlçz");

        ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity("/auth/register", validRegisterRequest, AuthenticationResponse.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().token());
    }

    @Test
    @DisplayName("Register method should return bad request when email is in a invalid format")
    void registerShouldReturnBadRequestWhenEmailHasAInvalidFormat() {
        final RegisterRequest validRegisterRequest = new RegisterRequest("validl.com", "Ae1!vlçz");

        ResponseEntity<BadRequestExceptionDetails> response = testRestTemplate.postForEntity("/auth/register", validRegisterRequest, BadRequestExceptionDetails.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getErrors().size());
        assertEquals("Invalid Arguments Exception", response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
    }

    @Test
    @DisplayName("Register method should return bad request when password is in a invalid format")
    void registerShouldReturnBadRequestWhenPasswordHasAInvalidFormat() {
        final RegisterRequest validRegisterRequest = new RegisterRequest("valid@email.com", "12345678");

        ResponseEntity<BadRequestExceptionDetails> response = testRestTemplate.postForEntity("/auth/register", validRegisterRequest, BadRequestExceptionDetails.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getErrors().size());
        assertEquals("Invalid Arguments Exception", response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
    }

    @Test
    @DisplayName("Register method should return bad request when password is blank")
    void registerShouldReturnBadRequestWhenPasswordIsBlank() {
        final RegisterRequest validRegisterRequest = new RegisterRequest("valid@email.com", "");

        ResponseEntity<BadRequestExceptionDetails> response = testRestTemplate.postForEntity("/auth/register", validRegisterRequest, BadRequestExceptionDetails.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getErrors().size());
        assertEquals("Invalid Arguments Exception", response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
    }

    @Test
    @DisplayName("Register method should return bad request when email is blank")
    void registerShouldReturnBadRequestWhenEmailIsBlank() {
        final RegisterRequest validRegisterRequest = new RegisterRequest("", "Ae1!vlçz");

        ResponseEntity<BadRequestExceptionDetails> response = testRestTemplate.postForEntity("/auth/register", validRegisterRequest, BadRequestExceptionDetails.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getErrors().size());
        assertEquals("Invalid Arguments Exception", response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
    }

    @Test
    @DisplayName("Register method should return bad request when email is already registered")
    void registerShouldReturnBadRequestWhenEmailIsAlreadyRegistered() {
        final RegisterRequest validRegisterRequest = new RegisterRequest("valid@email.com", "Ae1!vlçz");

        userRepository.save(User.builder()
                .email("valid@email.com")
                .password("Ae1!vlçz")
                .build()
        );

        ResponseEntity<BadRequestExceptionDetails> response = testRestTemplate.postForEntity("/auth/register", validRegisterRequest, BadRequestExceptionDetails.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.CONFLICT);
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getErrors().size());
        assertEquals("Invalid Arguments Exception", response.getBody().getMessage());
        assertEquals(HttpStatus.CONFLICT.value(), response.getBody().getStatus());
    }
}

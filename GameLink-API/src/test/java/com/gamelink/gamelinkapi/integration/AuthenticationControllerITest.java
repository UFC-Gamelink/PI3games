package com.gamelink.gamelinkapi.integration;

import com.gamelink.gamelinkapi.dtos.requests.authentication.AuthenticationRequest;
import com.gamelink.gamelinkapi.dtos.requests.authentication.RegisterRequest;
import com.gamelink.gamelinkapi.dtos.responses.authentication.AuthenticationResponse;
import com.gamelink.gamelinkapi.exceptions.RequestExceptionDetails;
import com.gamelink.gamelinkapi.models.users.User;
import com.gamelink.gamelinkapi.repositories.users.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthenticationControllerITest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Register method should return created status when is successful executed")
    void registerShouldExecuteWithSuccessWhenRequestHasAValidFormat() {
        final RegisterRequest validRegisterRequest = new RegisterRequest("username","valid@email.com", "Ae1!vlçz");

        ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity("/auth/register", validRegisterRequest, AuthenticationResponse.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().token());
    }

    @Test
    @DisplayName("Register method should return bad request when email is in a invalid format")
    void registerShouldReturnBadRequestWhenEmailHasAInvalidFormat() {
        final RegisterRequest validRegisterRequest = new RegisterRequest("username", "validl.com", "Ae1!vlçz");

        ResponseEntity<RequestExceptionDetails> response = testRestTemplate.postForEntity("/auth/register", validRegisterRequest, RequestExceptionDetails.class);

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
        final RegisterRequest validRegisterRequest = new RegisterRequest("username", "valid@email.com", "12345678");

        ResponseEntity<RequestExceptionDetails> response = testRestTemplate.postForEntity("/auth/register", validRegisterRequest, RequestExceptionDetails.class);

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
        final RegisterRequest validRegisterRequest = new RegisterRequest("username", "valid@email.com", "");

        ResponseEntity<RequestExceptionDetails> response = testRestTemplate.postForEntity("/auth/register", validRegisterRequest, RequestExceptionDetails.class);

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
        final RegisterRequest validRegisterRequest = new RegisterRequest("username", "", "Ae1!vlçz");

        ResponseEntity<RequestExceptionDetails> response = testRestTemplate.postForEntity("/auth/register", validRegisterRequest, RequestExceptionDetails.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getErrors().size());
        assertEquals("Invalid Arguments Exception", response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
    }

    @Test
    @DisplayName("Register method should return conflict request when email is already registered")
    void registerShouldReturnConflictRequestWhenEmailIsAlreadyRegistered() {
        final RegisterRequest validRegisterRequest = new RegisterRequest("username", "valid@email.com", "Ae1!vlçz");

        userRepository.save(User.builder()
                .username("novo")
                .email("valid@email.com")
                .password("Ae1!vlçz")
                .build()
        );

        ResponseEntity<RequestExceptionDetails> response = testRestTemplate.postForEntity("/auth/register", validRegisterRequest, RequestExceptionDetails.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.CONFLICT);
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getErrors().size());
        assertEquals("Invalid Arguments Exception", response.getBody().getMessage());
        assertEquals(HttpStatus.CONFLICT.value(), response.getBody().getStatus());
    }

    @Test
    @DisplayName("Register method should return conflict request when username is already registered")
    void registerShouldReturnConflictRequestWhenUsernameIsAlreadyRegistered() {
        final RegisterRequest validRegisterRequest = new RegisterRequest("username", "valid@email.com", "Ae1!vlçz");

        userRepository.save(User.builder()
                .username("username")
                .email("newemail@email.com")
                .password("Ae1!vlçz")
                .build()
        );

        ResponseEntity<RequestExceptionDetails> response = testRestTemplate.postForEntity("/auth/register", validRegisterRequest, RequestExceptionDetails.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.CONFLICT);
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getErrors().size());
        assertEquals("Invalid Arguments Exception", response.getBody().getMessage());
        assertEquals(HttpStatus.CONFLICT.value(), response.getBody().getStatus());
    }

    @Test
    @DisplayName("Register method should return bad request when username is blank")
    void registerShouldReturnBadRequestWhenUsernameIsBlank() {
        final RegisterRequest validRegisterRequest = new RegisterRequest("", "valid@email.com", "Ae1!vlçz");

        ResponseEntity<RequestExceptionDetails> response = testRestTemplate.postForEntity("/auth/register", validRegisterRequest, RequestExceptionDetails.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getErrors().size());
        assertEquals("Invalid Arguments Exception", response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
    }

    @Test
    @DisplayName("Authenticate method should return bad request when username is blank")
    void AuthenticationShouldReturnBadRequestWhenUsernameIsBlank() {
        final AuthenticationRequest validAuthRequest = new AuthenticationRequest("", "Ae1!vlçz");

        ResponseEntity<RequestExceptionDetails> response = testRestTemplate.postForEntity("/auth/authenticate", validAuthRequest, RequestExceptionDetails.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getErrors().size());
        assertEquals("Invalid Arguments Exception", response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
    }

    @Test
    @DisplayName("Authenticate method should return bad request when password is blank")
    void AuthenticationShouldReturnBadRequestWhenPasswordIsBlank() {
        final AuthenticationRequest validAuthRequest = new AuthenticationRequest("sold", "");

        ResponseEntity<RequestExceptionDetails> response = testRestTemplate.postForEntity("/auth/authenticate", validAuthRequest, RequestExceptionDetails.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getErrors().size());
        assertEquals("Invalid Arguments Exception", response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
    }

    @Test
    @DisplayName("Authenticate method should return success and a jwt token when is successful executed")
    void authenticateShouldExecuteWithSuccessWhenRequestHasAValidFormat() {
        final RegisterRequest validRegisterRequest = new RegisterRequest("username","valid@email.com", "Ae1!vlçz");
        final AuthenticationRequest validAuthRequest = new AuthenticationRequest("username","Ae1!vlçz");
        testRestTemplate.postForEntity("/auth/register", validRegisterRequest, AuthenticationResponse.class);

        ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity("/auth/authenticate", validAuthRequest, AuthenticationResponse.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().token());
    }
}

package com.gamelink.gamelinkapi.integration;

import com.gamelink.gamelinkapi.dtos.requests.authentication.RegisterRequest;
import com.gamelink.gamelinkapi.dtos.requests.users.PostUserProfileRequest;
import com.gamelink.gamelinkapi.dtos.responses.authentication.AuthenticationResponse;
import com.gamelink.gamelinkapi.dtos.responses.users.UserProfileResponse;
import com.gamelink.gamelinkapi.enums.Gender;
import com.gamelink.gamelinkapi.repositories.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.Objects;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserProfileControllerIT {
    private static final String URL = "/profile";
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private UserRepository userRepository;
    HttpHeaders headers = new HttpHeaders();

    @BeforeEach
    public void registerUser() {
        final RegisterRequest validRegisterRequest =
                new RegisterRequest("username","valid@email.com", "Ae1!vlçz");

        ResponseEntity<AuthenticationResponse> response = testRestTemplate
                .postForEntity("/auth/register", validRegisterRequest, AuthenticationResponse.class);

        var jwt = Objects.requireNonNull(response.getBody()).token();
        headers.set("Authorization", "Bearer " + jwt);
    }

    @Test
    @DisplayName("Post profile should return a created status when all values are valid")
    public void postProfileSuccess() {
        HttpEntity<PostUserProfileRequest> requestEntity;
        PostUserProfileRequest request = PostUserProfileRequest.builder()
                .name("sold")
                .bio("gamer")
                .birthdayDate(LocalDate.now())
                .gender(Gender.MALE)
                .build();
        requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<UserProfileResponse> response = testRestTemplate.exchange(
                URL,
                POST,
                requestEntity,
                UserProfileResponse.class
        );
        System.out.println(requestEntity.getHeaders());
        System.out.println(response);
    }
}

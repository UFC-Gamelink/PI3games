package com.gamelink.gamelinkapi.services;

import com.gamelink.gamelinkapi.dtos.requests.RegisterRequest;
import com.gamelink.gamelinkapi.dtos.responses.AuthenticationResponse;
import com.gamelink.gamelinkapi.models.User;
import com.gamelink.gamelinkapi.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class AuthenticationServiceTest {
    @Autowired
    private AuthenticationService authenticationService;
    @MockBean
    private UserRepository userRepository;

    @Test
    @DisplayName("Register should execute save in user repository and return a valid jwt token")
    void registerShouldSaveAUserAndReturnAValidJwtWhenSuccess(){
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        var userRequest = new RegisterRequest("username", "valid@email.com", "@Aa1abcd");

        AuthenticationResponse register = authenticationService.register(userRequest);

        assertNotNull(register);
        assertNotNull(register.token());
        assertTrue(register.token().length() > 0);

        verify(userRepository, times(1)).save(userCaptor.capture());
        assertEquals(userRequest.username(), userCaptor.getValue().getUsername());
        assertEquals(userRequest.email(), userCaptor.getValue().getEmail());
    }
}

package com.gamelink.gamelinkapi.services;

import com.gamelink.gamelinkapi.dtos.requests.authentication.AuthenticationRequest;
import com.gamelink.gamelinkapi.dtos.requests.authentication.RegisterRequest;
import com.gamelink.gamelinkapi.dtos.responses.authentication.AuthenticationResponse;
import com.gamelink.gamelinkapi.models.user.User;
import com.gamelink.gamelinkapi.repositories.users.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthenticationServiceTest {
    @Autowired
    private AuthenticationService authenticationService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    @DisplayName("Register should execute save in user repository and return a valid jwt token")
    void registerShouldSaveAUserAndReturnAValidJwtWhenSuccess(){
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        var userRequest = new RegisterRequest("username", "valid@email.com", "@Aa1abcd");

        AuthenticationResponse register = authenticationService.register(userRequest);

        assertNotNull(register);
        assertNotNull(register.token());
        assertFalse(register.token().isEmpty());

        verify(userRepository, times(1)).save(userCaptor.capture());
        assertEquals(userRequest.username(), userCaptor.getValue().getUsername());
        assertEquals(userRequest.email(), userCaptor.getValue().getEmail());
    }

    @Test
    @DisplayName("Authenticate should execute authenticate from AuthenticationManager in user repository and return a valid jwt token when find a user in database")
    void AuthenticateShouldFindAUserAndReturnAValidJwtWhenSuccess(){
        var userRequest = new AuthenticationRequest("username", "@Aa1abcd");
        when(userRepository.findUserByUsername("username"))
                .thenReturn(
                        Optional.of(User.builder().username("username").build())
                );

        AuthenticationResponse register = authenticationService.authenticate(userRequest);

        assertNotNull(register);
        assertNotNull(register.token());
        assertFalse(register.token().isEmpty());
        verify(userRepository, times(1)).findUserByUsername("username");
        verify(authenticationManager, times(1)).authenticate(ArgumentMatchers.any());
    }
}

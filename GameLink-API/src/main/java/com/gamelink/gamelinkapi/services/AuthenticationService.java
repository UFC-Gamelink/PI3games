package com.gamelink.gamelinkapi.services;

import com.gamelink.gamelinkapi.config.JwtService;
import com.gamelink.gamelinkapi.dtos.requests.RegisterRequest;
import com.gamelink.gamelinkapi.dtos.responses.AuthenticationResponse;
import com.gamelink.gamelinkapi.enums.Role;
import com.gamelink.gamelinkapi.models.User;
import com.gamelink.gamelinkapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();
        userRepository.save(user);

        return new AuthenticationResponse(jwtService.generateToken(user));
    }
}

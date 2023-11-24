package com.gamelink.gamelinkapi.services.users;

import com.gamelink.gamelinkapi.config.JwtService;
import com.gamelink.gamelinkapi.dtos.requests.authentication.AuthenticationRequest;
import com.gamelink.gamelinkapi.dtos.requests.authentication.RegisterRequest;
import com.gamelink.gamelinkapi.dtos.responses.authentication.AuthenticationResponse;
import com.gamelink.gamelinkapi.enums.Role;
import com.gamelink.gamelinkapi.models.users.User;
import com.gamelink.gamelinkapi.repositories.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();
        userRepository.save(user);

        return new AuthenticationResponse(jwtService.generateToken(user), user.getId());
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        var user = userRepository.findUserByUsername(request.username()).orElseThrow();

        return new AuthenticationResponse(jwtService.generateToken(user), user.getId());
    }
}

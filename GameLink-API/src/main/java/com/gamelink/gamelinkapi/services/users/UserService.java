package com.gamelink.gamelinkapi.services.users;

import com.gamelink.gamelinkapi.models.users.User;
import com.gamelink.gamelinkapi.repositories.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findUserAuthenticationContextOrThrowsBadRequestException() {
        return userRepository.findUserByUsername(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName()
        ).orElseThrow(() -> new BadCredentialsException("Invalid user"));
    }
}

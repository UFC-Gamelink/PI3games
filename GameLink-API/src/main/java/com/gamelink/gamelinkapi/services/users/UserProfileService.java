package com.gamelink.gamelinkapi.services.users;

import com.gamelink.gamelinkapi.dtos.requests.users.UserProfileRequest;
import com.gamelink.gamelinkapi.dtos.responses.users.UserProfileResponse;
import com.gamelink.gamelinkapi.mappers.UserProfileMapper;
import com.gamelink.gamelinkapi.models.users.User;
import com.gamelink.gamelinkapi.models.users.UserProfile;
import com.gamelink.gamelinkapi.repositories.users.UserProfileRepository;
import com.gamelink.gamelinkapi.repositories.users.UserRepository;
import com.gamelink.gamelinkapi.services.ICrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserProfileService implements ICrudService<UserProfile, UserProfileRequest, UserProfileResponse> {
    private final UserProfileRepository userProfileRepository;
    private final UserService userService;
    private final UserProfileMapper mapper = UserProfileMapper.INSTANCE;
    @Override
    public UserProfileResponse save(UserProfileRequest userProfileRequest) {
        UserProfile userProfileToBeSaved = mapper.requestToModel(userProfileRequest);
        User user = userService.findUserAuthenticationContextOrThrowsBadRequestException();

        userProfileToBeSaved.setUser(user);
        UserProfile userProfileSaved = userProfileRepository.save(userProfileToBeSaved);
        return mapper.modelToResponseDto(userProfileSaved);
    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public UserProfileResponse findById(UUID id) {
        return null;
    }
}

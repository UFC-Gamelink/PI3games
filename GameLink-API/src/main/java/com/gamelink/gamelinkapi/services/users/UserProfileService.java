package com.gamelink.gamelinkapi.services.users;

import com.gamelink.gamelinkapi.dtos.requests.users.UserProfileRequest;
import com.gamelink.gamelinkapi.dtos.responses.users.UserProfileResponse;
import com.gamelink.gamelinkapi.mappers.UserProfileMapper;
import com.gamelink.gamelinkapi.models.users.User;
import com.gamelink.gamelinkapi.models.users.UserProfile;
import com.gamelink.gamelinkapi.repositories.users.UserProfileRepository;
import com.gamelink.gamelinkapi.services.ICrudService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
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
        User user = userService.findUserAuthenticationContextOrThrowsBadCredentialException();

        userProfileToBeSaved.setUser(user);
        UserProfile userProfileSaved = userProfileRepository.save(userProfileToBeSaved);
        return mapper.modelToResponseDto(userProfileSaved);
    }

    @Override
    public void delete(UUID id) {
        User user = userService.findUserAuthenticationContextOrThrowsBadCredentialException();
        UserProfile userFounded = userProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("This user doesn't exists"));

        if (userFounded.getUser().getId().equals(user.getId())) {
            userProfileRepository.deleteById(id);
        } else {
            throw new BadCredentialsException("Invalid user");
        }
    }

    public UserProfileResponse findUserProfile() {
        User user = userService.findUserAuthenticationContextOrThrowsBadCredentialException();
        UserProfile userProfile = userProfileRepository.findUserProfileByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("This user doesn't exists"));

        return mapper.modelToResponseDto(userProfile);
    }
}

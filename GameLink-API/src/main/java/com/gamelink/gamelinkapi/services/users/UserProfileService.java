package com.gamelink.gamelinkapi.services.users;

import com.gamelink.gamelinkapi.dtos.requests.users.PostUserProfileRequest;
import com.gamelink.gamelinkapi.dtos.requests.users.PutUserProfileRequest;
import com.gamelink.gamelinkapi.dtos.responses.users.UserProfileResponse;
import com.gamelink.gamelinkapi.exceptions.SaveImageException;
import com.gamelink.gamelinkapi.mappers.UserProfileMapper;
import com.gamelink.gamelinkapi.models.images.ImageModel;
import com.gamelink.gamelinkapi.models.users.User;
import com.gamelink.gamelinkapi.models.users.UserProfile;
import com.gamelink.gamelinkapi.repositories.images.CloudinaryRepository;
import com.gamelink.gamelinkapi.repositories.users.UserProfileRepository;
import com.gamelink.gamelinkapi.services.ICrudService;
import com.gamelink.gamelinkapi.utils.Utils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService implements ICrudService<UserProfile, PostUserProfileRequest, UserProfileResponse> {
    private final UserProfileRepository userProfileRepository;
    private final UserService userService;
    private final CloudinaryRepository cloudinaryRepository;
    private final UserProfileMapper mapper = UserProfileMapper.INSTANCE;
    @Override
    public UserProfileResponse save(PostUserProfileRequest postUserProfileRequest) {
        UserProfile userProfileToBeSaved = mapper.postRequestToModel(postUserProfileRequest);
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
        UserProfile userProfile = findUserProfileIfExists();
        return mapper.modelToResponseDto(userProfile);
    }

    public UserProfileResponse updateProfile(PutUserProfileRequest userProfile) {
        User user = userService.findUserAuthenticationContextOrThrowsBadCredentialException();

        UserProfile oldUserProfile = userProfileRepository.findById(userProfile.id())
                .orElseThrow(() -> new EntityNotFoundException("User Profile not exists"));

        if (!userProfile.owner().equals(user.getUsername())){
            throw new BadCredentialsException("Invalid user");
        }

        Utils.copyNonNullProperties(mapper.putRequestToModel(userProfile), oldUserProfile);

        UserProfile userProfileUpdate = userProfileRepository.save(oldUserProfile);
        return mapper.modelToResponseDto(userProfileUpdate);
    }

    @Transactional
    public UserProfileResponse saveImages(MultipartFile icon, MultipartFile banner) throws SaveImageException {
        UserProfile myUserProfile = findUserProfileIfExists();
        ImageModel iconSaved;
        ImageModel bannerSaved;

        try {
            iconSaved = cloudinaryRepository.saveImage(icon);
            bannerSaved = cloudinaryRepository.saveImage(banner);
        } catch (IOException e) {
            throw new SaveImageException("Error during saving images");
        }

        myUserProfile.setIcon(iconSaved);
        myUserProfile.setBanner(bannerSaved);
        UserProfile userProfileUpdated = userProfileRepository.save(myUserProfile);
        return mapper.modelToResponseDto(userProfileUpdated);
    }

    private UserProfile findUserProfileIfExists() {
        User user = userService.findUserAuthenticationContextOrThrowsBadCredentialException();
        return userProfileRepository.findUserProfileByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("This user doesn't exists"));
    }
}

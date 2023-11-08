package com.gamelink.gamelinkapi.services.users;

import com.gamelink.gamelinkapi.dtos.requests.users.PostUserProfileRequest;
import com.gamelink.gamelinkapi.dtos.requests.users.PutUserProfileRequest;
import com.gamelink.gamelinkapi.dtos.responses.users.UserProfileResponse;
import com.gamelink.gamelinkapi.exceptions.SaveThreatementException;
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
    public void save(PostUserProfileRequest postUserProfileRequest) {
        UserProfile userProfileToBeSaved = mapper.postRequestToModel(postUserProfileRequest);
        User user = userService.findUserAuthenticationContextOrThrowsBadCredentialException();

        userProfileToBeSaved.setUser(user);
        userProfileRepository.save(userProfileToBeSaved);
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
    public UserProfileResponse updateImages(MultipartFile icon, MultipartFile banner) throws SaveThreatementException {
        UserProfile myUserProfile = findUserProfileIfExists();
        if (myUserProfile.getIcon() != null && myUserProfile.getBanner() != null) {
            try {
                cloudinaryRepository.deleteImage(myUserProfile.getIcon().getPublicId());
                cloudinaryRepository.deleteImage(myUserProfile.getBanner().getPublicId());
            } catch (IOException e) {
                throw new SaveThreatementException("Delete image failed");
            }
            return mapper.modelToResponseDto(
                    saveIconAndBannerOrThrowsSaveImageException(myUserProfile, icon, banner)
            );
        } else {
            throw new SaveThreatementException("Images are already saved");
        }
    }

    @Transactional
    public UserProfileResponse saveImages(MultipartFile icon, MultipartFile banner) throws SaveThreatementException {
        UserProfile myUserProfile = findUserProfileIfExists();
        if (myUserProfile.getIcon() == null || myUserProfile.getBanner() == null) {
            return mapper.modelToResponseDto(
                    saveIconAndBannerOrThrowsSaveImageException(myUserProfile, icon, banner)
            );
        } else {
            throw new SaveThreatementException("Images are already saved");
        }
    }

    private UserProfile saveIconAndBannerOrThrowsSaveImageException(UserProfile myUserProfile, MultipartFile icon, MultipartFile banner) {
        ImageModel iconSaved;
        ImageModel bannerSaved;

        try {
            iconSaved = cloudinaryRepository.saveImage(icon);
            bannerSaved = cloudinaryRepository.saveImage(banner);
        } catch (IOException e) {
            throw new SaveThreatementException("Error during saving images");
        }

        myUserProfile.setIcon(iconSaved);
        myUserProfile.setBanner(bannerSaved);
        UserProfile userProfileUpdated = userProfileRepository.save(myUserProfile);
        return userProfileUpdated;
    }
    private UserProfile findUserProfileIfExists() {
        User user = userService.findUserAuthenticationContextOrThrowsBadCredentialException();
        return userProfileRepository.findUserProfileByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("This user doesn't exists"));
    }
}

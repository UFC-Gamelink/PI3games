package com.gamelink.gamelinkapi.services;

import com.gamelink.gamelinkapi.dtos.requests.users.PostUserProfileRequest;
import com.gamelink.gamelinkapi.dtos.requests.users.PutUserProfileRequest;
import com.gamelink.gamelinkapi.dtos.responses.users.UserProfileResponse;
import com.gamelink.gamelinkapi.exceptions.SaveThreatementException;
import com.gamelink.gamelinkapi.mappers.UserProfileMapper;
import com.gamelink.gamelinkapi.models.ImageModel;
import com.gamelink.gamelinkapi.models.user.User;
import com.gamelink.gamelinkapi.models.user.UserProfile;
import com.gamelink.gamelinkapi.repositories.posts.PostRepository;
import com.gamelink.gamelinkapi.repositories.users.UserProfileRepository;
import com.gamelink.gamelinkapi.utils.PropertiesCopierUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService implements ICrudService<UserProfile, PostUserProfileRequest, UserProfileResponse> {
    private final UserProfileRepository userProfileRepository;
    private final UserService userService;
    private final ImageCloudService imageCloudService;
    private final PostRepository postRepository;
    private static final UserProfileMapper mapper = UserProfileMapper.INSTANCE;
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
            deleteBannerAndIcon(userFounded);
        } else {
            throw new BadCredentialsException("Invalid user");
        }
    }

    public UserProfileResponse findUserProfile() {
        UserProfile userProfile = findUserProfileByContext();
        return mapper.modelToResponseDto(
                userProfile,
                postRepository.countAllByOwner(userProfile)
        );
    }

    public UserProfileResponse updateProfile(PutUserProfileRequest userProfile) {
        User user = userService.findUserAuthenticationContextOrThrowsBadCredentialException();

        UserProfile oldUserProfile = userProfileRepository.findUserProfileByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("User Profile not exists"));

        if (!userProfile.owner().equals(user.getUsername())){
            throw new BadCredentialsException("Invalid user");
        }

        PropertiesCopierUtils.copyNonNullProperties(mapper.putRequestToModel(userProfile), oldUserProfile);

        UserProfile userProfileUpdate = userProfileRepository.save(oldUserProfile);
        return mapper.modelToResponseDto(
                userProfileUpdate,
                postRepository.countAllByOwner(userProfileUpdate)
        );
    }

    @Transactional
    public UserProfileResponse updateImages(MultipartFile icon, MultipartFile banner) throws SaveThreatementException {
        UserProfile myUserProfile = findUserProfileByContext();

        if (icon == null && banner == null) {
            throw new SaveThreatementException("There's no images defined");
        }

        if (icon != null) {
            ImageModel iconUpdated = imageCloudService.updateImageOrThrowSaveThreatementException(myUserProfile.getIcon(), icon);
            myUserProfile.setIcon(iconUpdated);
        }

        if (banner != null) {
            ImageModel bannerUpdated = imageCloudService.updateImageOrThrowSaveThreatementException(myUserProfile.getBanner(), banner);
            myUserProfile.setBanner(bannerUpdated);
        }

        UserProfile userUpdated = userProfileRepository.save(myUserProfile);

        return mapper.modelToResponseDto(
                userUpdated,
                postRepository.countAllByOwner(userUpdated)
        );
    }

    @Transactional
    public UserProfileResponse saveImages(MultipartFile icon, MultipartFile banner) throws SaveThreatementException {
        UserProfile myUserProfile = findUserProfileByContext();
        if (myUserProfile.getIcon() == null || myUserProfile.getBanner() == null) {
            UserProfile userUpdated = saveIconAndBannerOrThrowsSaveImageException(myUserProfile, icon, banner);
            return mapper.modelToResponseDto(
                    userUpdated,
                    postRepository.countAllByOwner(userUpdated)
            );
        } else {
            throw new SaveThreatementException("Images are already saved");
        }
    }

    public UserProfile findUserProfileByContext() {
        User user = userService.findUserAuthenticationContextOrThrowsBadCredentialException();
        return userProfileRepository.findUserProfileByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("This user doesn't exists"));
    }

    private void deleteBannerAndIcon(UserProfile myUserProfile) {
        imageCloudService.deleteImageOrThrowSaveThreatementException(myUserProfile.getIcon());
        imageCloudService.deleteImageOrThrowSaveThreatementException(myUserProfile.getBanner());
    }

    private UserProfile saveIconAndBannerOrThrowsSaveImageException(UserProfile myUserProfile, MultipartFile icon, MultipartFile banner) {
        myUserProfile.setIcon(imageCloudService.saveImageOrThrowSaveThreatementException(icon));
        myUserProfile.setBanner(imageCloudService.saveImageOrThrowSaveThreatementException(banner));

        return userProfileRepository.save(myUserProfile);
    }

}
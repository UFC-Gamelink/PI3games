package com.gamelink.gamelinkapi.services;

import com.gamelink.gamelinkapi.dtos.requests.users.PostUserProfileRequest;
import com.gamelink.gamelinkapi.dtos.responses.users.UserProfileResponse;
import com.gamelink.gamelinkapi.mappers.UserProfileMapper;
import com.gamelink.gamelinkapi.models.users.User;
import com.gamelink.gamelinkapi.models.users.UserProfile;
import com.gamelink.gamelinkapi.repositories.users.UserProfileRepository;
import com.gamelink.gamelinkapi.services.cloudinary.ImageCloudService;
import com.gamelink.gamelinkapi.services.users.UserProfileService;
import com.gamelink.gamelinkapi.services.users.UserService;
import com.gamelink.gamelinkapi.utils.creators.UserCreator;
import com.gamelink.gamelinkapi.utils.creators.UserProfileRequestCreator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserProfileServiceTest {
    @Autowired
    private UserProfileService service;
    private final UserProfileRequestCreator userProfileRequestCreator = UserProfileRequestCreator.getInstance();
    private final UserCreator userCreator = UserCreator.getInstance();
    private final UserProfileMapper userProfileMapper = UserProfileMapper.INSTANCE;
    @MockBean
    private UserProfileRepository userProfileRepository;
    @MockBean
    private UserService userService;
    @MockBean
    private ImageCloudService imageCloudService;

    @BeforeEach
    private void setup() {
        when(userProfileRepository.save(any(UserProfile.class)))
                .thenAnswer(invocation -> invocation.getArguments()[0]);
    }

    @Test
    @DisplayName("Post execute save in repository and return the entity saved")
    void postShouldExecuteSaveInRepositoryWhenSuccess() {
        PostUserProfileRequest postUserProfileRequest = userProfileRequestCreator.createValid();
        ArgumentCaptor<UserProfile> userProfileCaptor = ArgumentCaptor.forClass(UserProfile.class);

        service.save(postUserProfileRequest);

        verify(userProfileRepository, times(1)).save(userProfileCaptor.capture());
        verify(userService, times(1)).findUserAuthenticationContextOrThrowsBadCredentialException();
        assertEquals(postUserProfileRequest, userProfileMapper.modelToRequestDto(userProfileCaptor.getValue()));
    }

    @Test
    @DisplayName("Delete should verify if the user authenticated is the owner of the profile and delete when success")
    void deleteShouldVerifyAuthenticatedOwnerAndDeleteWhenSuccess(){
        final User validUser = userCreator.createValid();
        final UUID validId = validUser.getId();
        final UserProfile userProfile = UserProfile.builder().user(validUser).build();

        when(userService.findUserAuthenticationContextOrThrowsBadCredentialException())
                .thenReturn(validUser);
        when(userProfileRepository.findById(validId))
                .thenReturn(Optional.of(userProfile));

        service.delete(validId);
        verify(userProfileRepository, times(1)).deleteById(validId);
        verify(userService, times(1))
                .findUserAuthenticationContextOrThrowsBadCredentialException();
    }

    @Test
    @DisplayName("Delete should throws EntityNotFoundException If Theres not found the id")
    void deleteShouldThrowsEntityNotFoundExceptionsWhenTheresNotFoundTheId(){
        final User validUser = userCreator.createValid();
        final UUID validId = validUser.getId();

        when(userService.findUserAuthenticationContextOrThrowsBadCredentialException())
                .thenReturn(validUser);
        when(userProfileRepository.findById(validId))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> service.delete(validId));
        verify(userProfileRepository, times(1)).findById(validId);
        verify(userService, times(1))
                .findUserAuthenticationContextOrThrowsBadCredentialException();
    }

    @Test
    @DisplayName("Delete should throws BadCredentialsExceptions If Theres No A UserAuthenticated")
    void deleteShouldThrowsBadCredentialExceptionsWhenTheresNoAUserAuthenticated(){
        final User validUser = userCreator.createValid();
        final UUID validId = validUser.getId();

        when(userService.findUserAuthenticationContextOrThrowsBadCredentialException())
                .thenThrow(BadCredentialsException.class);

        assertThrows(BadCredentialsException.class, () -> service.delete(validId));
        verify(userService, times(1))
                .findUserAuthenticationContextOrThrowsBadCredentialException();
    }

    @Test
    @DisplayName("findUserProfile should verify if the user authenticated is the owner of the profile and return it when success")
    void findUserProfileShouldVerifyAuthenticatedOwnerAndReturnItWhenSuccess(){
        final User validUser = userCreator.createValid();
        final UserProfile userProfile = UserProfile.builder()
                .user(validUser)
                .build();
        userProfile.setCreatedAt(LocalDateTime.now());

        when(userService.findUserAuthenticationContextOrThrowsBadCredentialException())
                .thenReturn(validUser);
        when(userProfileRepository.findUserProfileByUser(validUser))
                .thenReturn(Optional.of(userProfile));

        service.findUserProfile();
        verify(userProfileRepository, times(1)).findUserProfileByUser(validUser);
        verify(userService, times(1))
                .findUserAuthenticationContextOrThrowsBadCredentialException();
    }

    @Test
    @DisplayName("findUserProfile should throws EntityNotFoundException If Theres not found user")
    void findUserProfileShouldThrowsEntityNotFoundExceptionsWhenTheresNotFoundUser(){
        final User validUser = userCreator.createValid();

        when(userService.findUserAuthenticationContextOrThrowsBadCredentialException())
                .thenReturn(validUser);
        when(userProfileRepository.findUserProfileByUser(validUser))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> service.findUserProfile());
        verify(userProfileRepository, times(1)).findUserProfileByUser(validUser);
        verify(userService, times(1))
                .findUserAuthenticationContextOrThrowsBadCredentialException();
    }

    @Test
    @DisplayName("findUserProfile should throws BadCredentialsExceptions If Theres No A UserAuthenticated")
    void findUserProfileShouldThrowsBadCredentialExceptionsWhenTheresNoAUserAuthenticated(){
        when(userService.findUserAuthenticationContextOrThrowsBadCredentialException())
                .thenThrow(BadCredentialsException.class);

        assertThrows(BadCredentialsException.class, () -> service.findUserProfile());
        verify(userService, times(1))
                .findUserAuthenticationContextOrThrowsBadCredentialException();
    }
}

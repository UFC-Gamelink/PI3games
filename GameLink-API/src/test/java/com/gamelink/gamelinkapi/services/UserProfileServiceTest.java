package com.gamelink.gamelinkapi.services;

import com.gamelink.gamelinkapi.dtos.requests.users.UserProfileRequest;
import com.gamelink.gamelinkapi.dtos.responses.users.UserProfileResponse;
import com.gamelink.gamelinkapi.mappers.UserProfileMapper;
import com.gamelink.gamelinkapi.models.users.User;
import com.gamelink.gamelinkapi.models.users.UserProfile;
import com.gamelink.gamelinkapi.repositories.users.UserProfileRepository;
import com.gamelink.gamelinkapi.services.users.UserProfileService;
import com.gamelink.gamelinkapi.services.users.UserService;
import com.gamelink.gamelinkapi.utils.creators.UserProfileRequestCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserProfileServiceTest {
    @Autowired
    private UserProfileService service;
    private final UserProfileRequestCreator userProfileRequestCreator = UserProfileRequestCreator.getInstance();
    private final UserProfileMapper userProfileMapper = UserProfileMapper.INSTANCE;
    @MockBean
    private UserProfileRepository userProfileRepository;
    @MockBean
    private UserService userService;

    @BeforeEach
    private void setup() {
        when(userService.findUserAuthenticationContextOrThrowsBadRequestException())
                .thenReturn(User.builder()
                        .username("username")
                        .email("valid@email.com")
                        .build());
        when(userProfileRepository.save(any(UserProfile.class)))
                .thenAnswer(invocation -> invocation.getArguments()[0]);
    }

    @Test
    @DisplayName("Post execute save in repository and return the entity saved")
    void postShouldExecuteSaveInRepositoryWhenSuccess() {
        UserProfileRequest userProfileRequest = userProfileRequestCreator.createValid();
        ArgumentCaptor<UserProfile> userProfileCaptor = ArgumentCaptor.forClass(UserProfile.class);

        UserProfileResponse userProfileSaved = service.save(userProfileRequest);

        Mockito.verify(userProfileRepository, times(1)).save(userProfileCaptor.capture());
        Assertions.assertEquals(userProfileRequest, userProfileMapper.modelToRequestDto(userProfileCaptor.getValue()));
        Assertions.assertEquals(userProfileRequest, userProfileMapper.responseToRequestDto(userProfileSaved));
    }
}

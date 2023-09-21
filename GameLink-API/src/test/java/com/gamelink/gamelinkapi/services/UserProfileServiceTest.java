package com.gamelink.gamelinkapi.services;

import com.gamelink.gamelinkapi.dtos.requests.users.UserProfileRequest;
import com.gamelink.gamelinkapi.dtos.responses.users.UserProfileResponse;
import com.gamelink.gamelinkapi.mappers.UserProfileMapper;
import com.gamelink.gamelinkapi.models.users.UserProfile;
import com.gamelink.gamelinkapi.repositories.users.UserProfileRepository;
import com.gamelink.gamelinkapi.repositories.users.UserRepository;
import com.gamelink.gamelinkapi.services.users.UserProfileService;
import com.gamelink.gamelinkapi.utils.creators.UserCreator;
import com.gamelink.gamelinkapi.utils.creators.UserProfileRequestCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

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
    private UserRepository userRepository;

    @BeforeEach
    private void setup() {
        SecurityContext securityContext = mock(SecurityContext.class, RETURNS_DEEP_STUBS);
        MockedStatic<SecurityContextHolder> securityContextHolderMockedStatic = Mockito.mockStatic(SecurityContextHolder.class);
        securityContextHolderMockedStatic
                .when(SecurityContextHolder::getContext)
                .thenReturn(securityContext);

        when(securityContext.getAuthentication().getName()).thenReturn("username");

        when(userProfileRepository.save(any(UserProfile.class)))
                .thenAnswer(invocation -> invocation.getArguments()[0]);

        when(userRepository.findUserByUsername(any()))
                .thenReturn(Optional.of(userCreator.createValid()));
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

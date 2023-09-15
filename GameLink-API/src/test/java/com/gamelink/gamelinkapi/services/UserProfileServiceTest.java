package com.gamelink.gamelinkapi.services;

import com.gamelink.gamelinkapi.dtos.requests.UserProfileRequest;
import com.gamelink.gamelinkapi.dtos.responses.UserProfileResponse;
import com.gamelink.gamelinkapi.mappers.UserProfileMapper;
import com.gamelink.gamelinkapi.models.UserProfile;
import com.gamelink.gamelinkapi.repositories.UserProfileRepository;
import com.gamelink.gamelinkapi.utils.creators.UserProfileRequestCreator;
import org.junit.jupiter.api.Assertions;
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
    private final UserProfileRequestCreator creator = UserProfileRequestCreator.getInstance();
    private final UserProfileMapper mapper = UserProfileMapper.INSTANCE;
    @MockBean
    private UserProfileRepository repository;

    @Test
    @DisplayName("Post execute save in repository and return the entity saved")
    void postShouldExecuteSaveInRepositoryWhenSuccess() {
        UserProfileRequest userProfileRequest = creator.createValid();
        ArgumentCaptor<UserProfile> userProfileCaptor = ArgumentCaptor.forClass(UserProfile.class);

        when(repository.save(any(UserProfile.class)))
                .thenAnswer(invocation -> invocation.getArguments()[0]);

        UserProfileResponse userProfileSaved = service.save(userProfileRequest);

        Mockito.verify(repository, times(1)).save(userProfileCaptor.capture());
        Assertions.assertEquals(userProfileRequest, mapper.modelToRequestDto(userProfileCaptor.getValue()));
        Assertions.assertEquals(userProfileRequest, mapper.responseToRequestDto(userProfileSaved));
    }
}

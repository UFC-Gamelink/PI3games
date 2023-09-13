package com.gamelink.gamelinkapi.services;

import com.gamelink.gamelinkapi.dtos.requests.UserProfileRequest;
import com.gamelink.gamelinkapi.models.UserProfile;
import com.gamelink.gamelinkapi.repositories.UserProfileRepository;
import com.gamelink.gamelinkapi.utils.creators.UserProfileRequestCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
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
    @MockBean
    private UserProfileRepository repository;

    @Test
    @DisplayName("Post execute save in repository and return the entity saved")
    void postShouldExecuteSaveInRepositoryWhenSuccess() {
        UserProfile userProfileValid = new UserProfile();
        UserProfileRequest userProfileRequest = creator.createValid();
        BeanUtils.copyProperties(userProfileValid, userProfileRequest);
        ArgumentCaptor<UserProfile> userProfileCaptor = ArgumentCaptor.forClass(UserProfile.class);

        when(repository.save(any(UserProfile.class)))
                .thenAnswer(invocation -> invocation.getArguments()[0]);

        UserProfileRequest userProfileSaved = service.save(userProfileRequest);

        Mockito.verify(repository, times(1)).save(userProfileCaptor.capture());
        Assertions.assertEquals(userProfileValid, userProfileSaved);
    }
}

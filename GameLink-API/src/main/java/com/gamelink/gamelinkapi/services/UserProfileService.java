package com.gamelink.gamelinkapi.services;

import com.gamelink.gamelinkapi.dtos.requests.UserProfileRequest;
import com.gamelink.gamelinkapi.models.UserProfile;
import com.gamelink.gamelinkapi.repositories.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

@RequiredArgsConstructor
public class UserProfileService implements ICrudService<UserProfile, UserProfileRequest>{
    private final UserProfileRepository repository;
    @Override
    public UserProfileRequest save(UserProfileRequest userProfileRequest) {
        UserProfile userProfile = new UserProfile();
        BeanUtils.copyProperties(userProfileRequest, userProfile);
        return null;
    }

    @Override
    public void delete(UserProfile entity) {

    }

    @Override
    public UserProfileRequest findById(UUID id) {
        return null;
    }
}

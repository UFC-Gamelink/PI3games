package com.gamelink.gamelinkapi.mappers;

import com.gamelink.gamelinkapi.dtos.requests.UserProfileRequest;
import com.gamelink.gamelinkapi.dtos.responses.UserProfileResponse;
import com.gamelink.gamelinkapi.models.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserProfileMapper extends ModelDtoMapper<UserProfile, UserProfileRequest, UserProfileResponse> {
    UserProfileMapper INSTANCE = Mappers.getMapper(UserProfileMapper.class);
    @Override
    UserProfileRequest modelToRequestDto(UserProfile userProfile);
    @Override
    UserProfile requestToModel(UserProfileRequest request);
    @Override
    UserProfileResponse modelToResponseDto(UserProfile userProfile);

    @Override
    UserProfileResponse requestToResponseDto(UserProfileRequest userProfileRequest);

    @Override
    UserProfileRequest responseToRequestDto(UserProfileResponse userProfileResponse);
}

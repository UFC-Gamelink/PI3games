package com.gamelink.gamelinkapi.mappers;

import com.gamelink.gamelinkapi.dtos.requests.users.PostUserProfileRequest;
import com.gamelink.gamelinkapi.dtos.requests.users.PutUserProfileRequest;
import com.gamelink.gamelinkapi.dtos.responses.users.UserProfileResponse;
import com.gamelink.gamelinkapi.models.users.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper
public interface UserProfileMapper {
    UserProfileMapper INSTANCE = Mappers.getMapper(UserProfileMapper.class);

    PostUserProfileRequest modelToRequestDto(UserProfile userProfile);

    UserProfile postRequestToModel(PostUserProfileRequest request);

    @Mapping(target = "owner", source = "userProfile.user.username")
    @Mapping(qualifiedByName = "entryDateMapper", target = "entryDate", source = "userProfile.createdAt")
    @Mapping(target = "qntPosts", source = "qntPosts")
    UserProfileResponse modelToResponseDto(UserProfile userProfile, int qntPosts);

    UserProfileResponse postRequestToResponseDto(PostUserProfileRequest postUserProfileRequest);

    PostUserProfileRequest responseToPostRequestDto(UserProfileResponse userProfileResponse);

    UserProfile putRequestToModel(PutUserProfileRequest putUserProfileRequest);

    @Named("entryDateMapper")
    static LocalDate entryDateMapper(LocalDateTime createdAt) {
        return createdAt.toLocalDate();
    }
}

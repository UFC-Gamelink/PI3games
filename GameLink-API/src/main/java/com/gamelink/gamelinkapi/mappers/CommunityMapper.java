package com.gamelink.gamelinkapi.mappers;

import com.gamelink.gamelinkapi.dtos.requests.communities.CommunityRequest;
import com.gamelink.gamelinkapi.dtos.responses.communities.CommunitiesGeneralResponse;
import com.gamelink.gamelinkapi.dtos.responses.communities.CommunityResponse;
import com.gamelink.gamelinkapi.models.comunities.CommunityModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = PostMapper.class)
public interface CommunityMapper {
    CommunityMapper INSTANCE = Mappers.getMapper(CommunityMapper.class);

    CommunityModel requestToModel(CommunityRequest communityRequest);
    @Mapping(target = "owner", source = "owner.username")
    @Mapping(target = "ownerId", source = "owner.id")
    @Mapping(target = "bannerUrl", source = "banner.url")
    CommunityResponse modelToResponse(CommunityModel communityModel);

    @Mapping(target = "owner", source = "owner.username")
    @Mapping(target = "ownerId", source = "owner.id")
    @Mapping(target = "bannerUrl", source = "banner.url")
    CommunitiesGeneralResponse modelToGeneralResponse(CommunityModel communityModel);
}

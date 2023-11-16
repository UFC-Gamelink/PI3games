package com.gamelink.gamelinkapi.mappers;

import com.gamelink.gamelinkapi.dtos.requests.posts.PostRequest;
import com.gamelink.gamelinkapi.dtos.responses.posts.PostResponse;
import com.gamelink.gamelinkapi.models.posts.PostModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);
    PostModel requestToModel(PostRequest postRequest);
    @Mapping(target = "imageUrl", source = "image.url")
    @Mapping(target = "postDate", source = "createdAt")
    PostResponse modelToResponse(PostModel postModel);

}

package com.gamelink.gamelinkapi.mappers;

import com.gamelink.gamelinkapi.dtos.requests.commentaries.CommentaryRequest;
import com.gamelink.gamelinkapi.dtos.responses.commentaries.CommentaryResponse;
import com.gamelink.gamelinkapi.models.CommentaryModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentaryMapper {
    CommentaryMapper INSTANCE = Mappers.getMapper(CommentaryMapper.class);
    
    CommentaryModel requestToModel(CommentaryRequest commentaryRequest);

    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "ownerId", source = "creator.user.id")
    @Mapping(target = "ownerName", source = "creator.name")
    @Mapping(target = "username", source = "creator.user.username")
    @Mapping(target = "userIconUrl", source = "creator.icon.url")
    CommentaryResponse modelToResponse(CommentaryModel commentarySaved);
}

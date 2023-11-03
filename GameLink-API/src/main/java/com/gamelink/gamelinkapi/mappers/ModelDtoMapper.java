package com.gamelink.gamelinkapi.mappers;

import com.gamelink.gamelinkapi.models.BaseModel;
import org.springframework.stereotype.Component;

@Component
public interface ModelDtoMapper<M extends BaseModel, POST_REQUEST, R> {
    POST_REQUEST modelToRequestDto(M m);
    R modelToResponseDto(M m);
    M postRequestToModel(POST_REQUEST postRequest);
    R postRequestToResponseDto(POST_REQUEST postRequest);
    POST_REQUEST responseToPostRequestDto(R r);
}

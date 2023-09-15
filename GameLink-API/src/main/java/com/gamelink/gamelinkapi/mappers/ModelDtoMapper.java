package com.gamelink.gamelinkapi.mappers;

import com.gamelink.gamelinkapi.models.BaseModel;
import org.springframework.stereotype.Component;

@Component
public interface ModelDtoMapper<M extends BaseModel, D, R> {
    D modelToRequestDto(M m);
    R modelToResponseDto(M m);
    M requestToModel(D d);
    R requestToResponseDto(D d);
    D responseToRequestDto(R r);
}

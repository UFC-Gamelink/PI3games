package com.gamelink.gamelinkapi.services;

import com.gamelink.gamelinkapi.dtos.BaseDto;
import com.gamelink.gamelinkapi.models.BaseEntity;

import java.util.UUID;

public interface ICrudService<E extends BaseEntity, D> {
    public D save(D entityDto) ;
    void delete(E entity);
    D findById(UUID id);
}

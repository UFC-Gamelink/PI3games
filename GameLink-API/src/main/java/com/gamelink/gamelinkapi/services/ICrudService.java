package com.gamelink.gamelinkapi.services;

import com.gamelink.gamelinkapi.models.BaseModel;

import java.util.UUID;

public interface ICrudService<E extends BaseModel, D, R> {
    public R save(D entityDto) ;
    void delete(E entity);
    R findById(UUID id);
}

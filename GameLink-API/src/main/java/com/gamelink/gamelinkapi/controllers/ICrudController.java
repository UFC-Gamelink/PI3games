package com.gamelink.gamelinkapi.controllers;

import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ICrudController<R> {
    ResponseEntity<Void> post(R requestDto) ;
    ResponseEntity<Void> delete(UUID id);
}

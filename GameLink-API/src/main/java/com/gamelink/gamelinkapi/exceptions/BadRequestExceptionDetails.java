package com.gamelink.gamelinkapi.exceptions;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BadRequestExceptionDetails {
    private String message;
    private int status;
    private String details;
    private LocalDateTime timestamp;
    private List<String> errors;
}

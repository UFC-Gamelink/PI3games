package com.gamelink.gamelinkapi.handler;

import com.gamelink.gamelinkapi.exceptions.BadRequestExceptionDetails;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BadRequestExceptionDetails> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(value -> value.getField() + " " + value.getDefaultMessage())
                .collect(Collectors.toList());

        var exceptionDetails = BadRequestExceptionDetails.builder()
                .message("Invalid Arguments Exception")
                .details("")
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDetails);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<BadRequestExceptionDetails> handleIntegrityViolation(DataIntegrityViolationException ex) {
        var exceptionDetails = BadRequestExceptionDetails.builder()
                .message("Invalid Arguments Exception")
                .details("")
                .timestamp(LocalDateTime.now())
                .errors(List.of(getDetailsDataIntegrityViolationExceptionMessage(ex)))
                .status(HttpStatus.CONFLICT.value())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionDetails);
    }

    private String getDetailsDataIntegrityViolationExceptionMessage(DataIntegrityViolationException exception) {
        String message = exception.getRootCause().getLocalizedMessage();
        return message.substring(message.indexOf('\n') + 3);
    }
}

package com.gamelink.gamelinkapi.handler;

import com.gamelink.gamelinkapi.exceptions.RequestExceptionDetails;
import com.gamelink.gamelinkapi.exceptions.SaveThreatementException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<RequestExceptionDetails> handleEntityNotFound(EntityNotFoundException ex) {
        var exceptionDetails = RequestExceptionDetails.builder()
                .message("Entity not found")
                .details(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionDetails);
    }

    @ExceptionHandler(SaveThreatementException.class)
    public ResponseEntity<RequestExceptionDetails> handleSaveImageException(SaveThreatementException ex) {
        var exceptionDetails = RequestExceptionDetails.builder()
                .message("Save image in cloudinary failed")
                .details(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDetails);
    }

    @ExceptionHandler(InvalidPropertiesFormatException.class)
    public ResponseEntity<RequestExceptionDetails> handleInvalidPropertiesFormatException(InvalidPropertiesFormatException ex) {
        var exceptionDetails = RequestExceptionDetails.builder()
                .message("File format exception")
                .details(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDetails);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RequestExceptionDetails> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(value -> value.getField() + " " + value.getDefaultMessage())
                .collect(Collectors.toList());

        var exceptionDetails = RequestExceptionDetails.builder()
                .message("Invalid Arguments Exception")
                .details("")
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDetails);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<RequestExceptionDetails> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        List<String> errors = List.of(ex.getCause().getMessage());

        var exceptionDetails = RequestExceptionDetails.builder()
                .message("User not found")
                .details("")
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .status(HttpStatus.UNAUTHORIZED.value())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionDetails);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<RequestExceptionDetails> handleIntegrityViolation(DataIntegrityViolationException ex) {
        var exceptionDetails = RequestExceptionDetails.builder()
                .message("Invalid Arguments Exception")
                .details("")
                .timestamp(LocalDateTime.now())
                .errors(List.of(getDetailsDataIntegrityViolationExceptionMessage(ex)))
                .status(HttpStatus.CONFLICT.value())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionDetails);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<RequestExceptionDetails> handleBadCredentialsException(BadCredentialsException exception) {
        return ResponseEntity.noContent().build();
    }

    private String getDetailsDataIntegrityViolationExceptionMessage(DataIntegrityViolationException exception) {
        String message = exception.getRootCause().getLocalizedMessage();
        return message.substring(message.indexOf('\n') + 3);
    }
}

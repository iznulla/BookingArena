package com.booking.arena.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.UUID;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<?> catchResourceNotFoundException(ResourceNotFoundException e) {
        AppError appError = new AppError(UUID.randomUUID(), e.getMessage());
        log.error("ID " + appError.getId().toString(), e);
        return new ResponseEntity<>(("ERROR: " + appError.getMessage() + "\nid: " + appError.getId()), HttpStatus.NOT_FOUND);
    }
}

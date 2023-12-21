package com.booking.arena.exception;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
public class AppError extends RuntimeException {
    private final UUID id;
    private final String message;

    public AppError(UUID id, String message) {
        this.id = id;
        this.message = message;
    }
}

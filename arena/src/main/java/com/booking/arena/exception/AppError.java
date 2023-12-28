package com.booking.arena.exception;

import lombok.Getter;

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

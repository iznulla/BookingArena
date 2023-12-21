package com.booking.arena.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponseDto {
    private final String accessToken;
}

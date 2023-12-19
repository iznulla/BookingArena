package com.booking.arena.dto.login;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponseDto {
    private final String accessToken;
}

package com.booking.arena.dto.user.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class UserUpdateDto {
    private String username;
    private String email;
    private String role;
    private boolean isActive;
}

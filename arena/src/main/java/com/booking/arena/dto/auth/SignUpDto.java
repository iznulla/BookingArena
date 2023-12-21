package com.booking.arena.dto.auth;

import com.booking.arena.dto.user.RoleDto;
import com.booking.arena.dto.user.UserProfileDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignUpDto {
    private String username;
    private String password;
    private String email;
    private UserProfileDto userProfile;
}

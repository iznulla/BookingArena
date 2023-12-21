package com.booking.arena.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "User data transfer object")
public class UserDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, hidden = true)
    @JsonIgnore
    private Long id;
    private String username;
    private RoleDto role;
    private String email;
    private boolean isActive;
    private UserProfileDto userProfile;
}

package com.booking.arena.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrivilegeDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, hidden = true)
    @JsonIgnore
    private Long id;
    private String name;
}

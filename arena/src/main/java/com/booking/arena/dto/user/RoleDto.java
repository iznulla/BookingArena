package com.booking.arena.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, hidden = true)
    @JsonIgnore
    private Long id;
    private String name;
    private List<RolePrivilegeDto> rolePrivileges;
}
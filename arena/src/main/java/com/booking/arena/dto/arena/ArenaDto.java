package com.booking.arena.dto.arena;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArenaDto {
    private Long id;
    private String name;
    private String description;
    private boolean status = true;
    private ArenaInfoDto arenaInfo;
    @JsonIgnore
    private MultipartFile imageFile;
    private String image;
}

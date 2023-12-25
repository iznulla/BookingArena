package com.booking.arena.dto.arena;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArenaFiltersDto {
    private String city;
    private Double longitude;
    private Double latitude;
    private Instant from;
    private Instant to;
}

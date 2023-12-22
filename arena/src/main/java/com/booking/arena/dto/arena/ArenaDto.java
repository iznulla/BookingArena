package com.booking.arena.dto.arena;

import com.booking.arena.dto.booking.ReservationArenaDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



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
}

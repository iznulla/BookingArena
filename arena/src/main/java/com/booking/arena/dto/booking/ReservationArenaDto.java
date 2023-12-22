package com.booking.arena.dto.booking;

import com.booking.arena.entity.arena.ArenaEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationArenaDto {
    private Long id;
    private Instant createdAt;
    private Instant bookingFrom;
    private Instant bookingTo;
    private String description;
    private Integer totalPrice;
    private String costumer;
    private ArenaEntity arena;
}

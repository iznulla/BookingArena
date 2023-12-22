package com.booking.arena.dto.arena;

import com.booking.arena.dto.address.AddressDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArenaInfoDto {
    private Long id;
    private String phone;
    private Integer price;
    private Instant workedFrom;
    private Instant workedTo;
    private Instant createdAt;
    private Instant updatedAt;
    private AddressDto address;
}

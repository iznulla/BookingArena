package com.booking.arena.service.booking;

import com.booking.arena.dto.booking.ReservationArenaDto;

import java.util.List;
import java.util.Optional;

public interface ReservationService {
    Optional<ReservationArenaDto> getById(Long id);
    List<ReservationArenaDto> getAll();
    Optional<ReservationArenaDto> create(ReservationArenaDto reservationArenaDto);
    Optional<ReservationArenaDto> update(Long id, ReservationArenaDto reservationArenaDto);
    void delete(Long id);
}

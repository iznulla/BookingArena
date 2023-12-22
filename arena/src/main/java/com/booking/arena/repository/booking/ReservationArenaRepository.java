package com.booking.arena.repository.booking;

import com.booking.arena.entity.booking.ReservationArena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationArenaRepository extends JpaRepository<ReservationArena, Long> {
}

package com.booking.arena.repository.booking;

import com.booking.arena.entity.booking.BookingUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingUserRepository extends JpaRepository<BookingUser, Long> {
}

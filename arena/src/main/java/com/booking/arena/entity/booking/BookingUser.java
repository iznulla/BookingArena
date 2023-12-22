package com.booking.arena.entity.booking;

import com.booking.arena.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "consumer")
    private String consumer;
    @ManyToOne
    @JoinColumn(name = "reservation_arena_id", referencedColumnName = "id")
    private ReservationArena reservationArena;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    public void setBooking(ReservationArena reservationArena) {
        this.reservationArena = reservationArena;
        reservationArena.getBookingUser().add(this);
    }

    public void setUser(UserEntity user) {
        this.user = user;
        reservationArena.getBookingUser().add(this);
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public String getConsumer() {
        return consumer;
    }
}

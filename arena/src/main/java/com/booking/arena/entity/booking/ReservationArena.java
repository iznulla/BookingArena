package com.booking.arena.entity.booking;

import com.booking.arena.entity.arena.ArenaEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "reservation_arena")
public class ReservationArena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created_at")
    private Instant createdAt;
    @Column(name = "booking_from")
    private Instant bookingFrom;
    @Column(name = "booking_to")
    private Instant bookingTo;
    @Column(name = "description")
    private String description;
    @Column(name = "total_price")
    private Integer totalPrice;
    @Column(name = "costumer")
    private String costumer;

    @ManyToOne
    @JoinColumn(name = "arena_id", referencedColumnName = "id")
    private ArenaEntity arena;

    @Builder.Default
    @OneToMany(mappedBy = "reservationArena", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<BookingUser> bookingUser = new ArrayList<>();
}

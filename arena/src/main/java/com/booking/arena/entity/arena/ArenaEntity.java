package com.booking.arena.entity.arena;


import com.booking.arena.entity.booking.ReservationArena;
import com.booking.arena.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "arena")
public class ArenaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "status", columnDefinition = "boolean default true")
    private boolean status = true;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @OneToOne(mappedBy = "arena", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
    private ArenaInfo arenaInfo;

    @Builder.Default
    @OneToMany(mappedBy = "arena", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<ReservationArena> reservationArena = new ArrayList<>();
}

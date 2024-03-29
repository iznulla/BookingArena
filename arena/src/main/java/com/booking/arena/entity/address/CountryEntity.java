package com.booking.arena.entity.address;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "country")
public class CountryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<CityEntity> cities = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Address> addresses = new ArrayList<>();

    @Column(name = "created_at")
    private Instant createdAt;
    @Column(name = "updated_at")
    private Instant updatedAt;
}
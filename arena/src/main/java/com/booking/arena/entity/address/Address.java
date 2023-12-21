package com.booking.arena.entity.address;


import com.booking.arena.entity.user.UserProfile;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;


@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String street;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private CountryEntity country;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private CityEntity city;


    @OneToOne(mappedBy = "address")
    private UserProfile userProfile;

    @Column(name = "created_at")
    private Instant createdAt;
    @Column(name = "updated_at")
    private Instant updatedAt;

    private Double longitude;
    private Double latitude;
}

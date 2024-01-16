package com.booking.arena.entity.address;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "city")
public class CityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private CountryEntity country;

    @Builder.Default
    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Address> addresses = new ArrayList<>();

    @Column(name = "created_at")
    private Instant createdAt;
    @Column(name = "updated_at")
    private Instant updatedAt;

    public void setCountry(CountryEntity country) {
        this.country = country;
        country.getCities().add(this);
    }
}

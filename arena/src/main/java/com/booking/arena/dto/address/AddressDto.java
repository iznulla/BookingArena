package com.booking.arena.dto.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private String street;
    private LocationDto country;
    private CityDto city;
    private Double longitude;
    private Double latitude;
}

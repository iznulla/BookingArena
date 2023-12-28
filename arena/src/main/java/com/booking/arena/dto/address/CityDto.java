package com.booking.arena.dto.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityDto {
    @JsonIgnore
    private Long id;
    private String name;
    @JsonIgnoreProperties(value = {"country"}, allowGetters = true)
    private String countryName;
}

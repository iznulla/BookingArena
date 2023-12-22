package com.booking.arena.service.address.country;



import com.booking.arena.dto.address.LocationDto;

import java.util.List;
import java.util.Optional;

public interface CountryService {
    Optional<LocationDto> create(LocationDto countryDto);
    Optional<LocationDto> update(Long id, LocationDto countryDto);
    Optional<LocationDto> getById(Long id);
    Optional<LocationDto> getCountryByName(String name);
    List<LocationDto> getAll();
    void delete(Long id);
}

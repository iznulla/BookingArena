package com.booking.arena.service.address.country;



import com.booking.arena.dto.address.LocationDto;

import java.util.List;
import java.util.Optional;

public interface CountryService {
    Optional<LocationDto> createCountry(LocationDto countryDto);
    Optional<LocationDto> updateCountry(Long id, LocationDto countryDto);
    Optional<LocationDto> getCountryById(Long id);
    Optional<LocationDto> getCountryByName(String name);
    List<LocationDto> getAllCountries();
    void deleteCountryById(Long id);
}

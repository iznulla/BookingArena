package com.booking.arena.service.address.country;



import com.booking.arena.dto.address.CountryDto;

import java.util.List;
import java.util.Optional;

public interface CountryService {
    Optional<CountryDto> create(CountryDto countryDto);
    Optional<CountryDto> update(Long id, CountryDto countryDto);
    Optional<CountryDto> getById(Long id);
    Optional<CountryDto> getCountryByName(String name);
    List<CountryDto> getAll();
    void delete(Long id);
}

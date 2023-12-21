package com.booking.arena.service.address.city;



import com.booking.arena.dto.address.LocationDto;

import java.util.List;
import java.util.Optional;

public interface CityService {
    Optional<LocationDto> createCity(LocationDto cityDto);
    Optional<LocationDto> updateCity(Long id, LocationDto cityDto);
    Optional<LocationDto> getCityById(Long id);
    Optional<LocationDto> getCityByName(String name);
    Optional<List<LocationDto>> getAllCities();
    void deleteCityById(Long id);
}

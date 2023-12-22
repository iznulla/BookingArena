package com.booking.arena.service.address.city;



import com.booking.arena.dto.address.CityDto;

import java.util.List;
import java.util.Optional;

public interface CityService {

    Optional<CityDto> create(CityDto cityDto);

    Optional<CityDto> update(Long id, CityDto cityDto);
    Optional<CityDto> getById(Long id);
    Optional<CityDto> getCityByName(String name);
    Optional<List<CityDto>> getAll();
    void delete(Long id);
}

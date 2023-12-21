package com.booking.arena.service.address.city;


import com.booking.arena.dto.address.LocationDto;
import com.booking.arena.entity.address.CityEntity;
import com.booking.arena.entity.address.CountryEntity;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.repository.CityRepository;
import com.booking.arena.repository.CountryRepository;
import com.booking.arena.utils.ConvertEntityToDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    @Override
    public Optional<LocationDto> createCity(LocationDto cityDto) {
        CountryEntity country = countryRepository.findById(cityDto.getId()).orElseThrow(() ->
                new ResourceNotFoundException("Not found country with id: " + cityDto.getId()));
        CityEntity city = CityEntity.builder()
                .name(cityDto.getName())
                .build();
        city.setCountry(country);
        city.setCreatedAt(Instant.now());
        city.setUpdatedAt(Instant.now());
        cityRepository.save(city);
        log.info("City created: {}", city.getName());
        return Optional.of(LocationDto.builder()
                .name(city.getName())
                .build());
    }

    @Override
    public Optional<LocationDto> updateCity(Long id, LocationDto cityDto) {
        CityEntity city = cityRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Not found city with id: " + id));
        city.setName(cityDto.getName());
        city.setUpdatedAt(Instant.now());
        cityRepository.save(city);
        log.info("City updated: {}", city.getName());
        return Optional.of(LocationDto.builder()
                .name(city.getName())
                .build());
    }

    @Override
    public Optional<LocationDto> getCityById(Long id) {
        return Optional.of(LocationDto.builder()
                .name(cityRepository.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException("Not found city with id: " + id)).getName())
                .build());
    }

    @Override
    public Optional<LocationDto> getCityByName(String name) {
        return Optional.of(LocationDto.builder()
                .name(cityRepository.findByName(name).orElseThrow(() ->
                        new ResourceNotFoundException("Not found city with id: " + name)).getName())
                .build());
    }

    @Override
    public Optional<List<LocationDto>> getAllCities() {
        return Optional.of(cityRepository.findAll().stream()
                .map(ConvertEntityToDto::cityToDto).collect(Collectors.toList()));
    }

    @Override
    public void deleteCityById(Long id) {
        log.info("City deleted by id: {}", id);
        cityRepository.deleteById(id);
    }
}

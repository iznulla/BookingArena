package com.booking.arena.service.address.city;


import com.booking.arena.dto.address.CityDto;
import com.booking.arena.entity.address.CityEntity;
import com.booking.arena.entity.address.CountryEntity;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.repository.address.CityRepository;
import com.booking.arena.repository.address.CountryRepository;
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
    public Optional<CityDto> create(CityDto cityDto) {
        CountryEntity country = countryRepository.findByName(cityDto.getCountryName()).orElseThrow(() ->
                new ResourceNotFoundException("Not found country with id: " + cityDto.getId()));
        CityEntity city = CityEntity.builder()
                .name(cityDto.getName())
                .build();
        city.setCountry(country);
        city.setCreatedAt(Instant.now());
        city.setUpdatedAt(Instant.now());
        cityRepository.save(city);
        log.info("City created: {}", city.getName());
        return Optional.of(CityDto.builder()
                .name(city.getName())
                .build());
    }

    @Override
    public Optional<CityDto> update(Long id, CityDto cityDto) {
        CityEntity city = cityRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Not found city with id: " + id));
        city.setName(cityDto.getName());
        city.setUpdatedAt(Instant.now());
        cityRepository.save(city);
        log.info("City updated: {}", city.getName());
        return Optional.of(CityDto.builder()
                .name(city.getName())
                .build());
    }

    @Override
    public Optional<CityDto> getById(Long id) {
        CityEntity city = cityRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Not found city with id: " + id));
        return Optional.of(CityDto.builder()
                .name(city.getName())
                .countryName(city.getCountry().getName())
                .build());
    }

    @Override
    public Optional<CityDto> getCityByName(String name) {
        CityEntity city = cityRepository.findByName(name).orElseThrow(() ->
                new ResourceNotFoundException("Not found city with name: " + name));
        return Optional.of(CityDto.builder()
                .name(city.getName())
                .countryName(city.getCountry().getName())
                .build());
    }

    @Override
    public Optional<List<CityDto>> getAll() {
        return Optional.of(cityRepository.findAll().stream()
                .map(ConvertEntityToDto::cityToDto).collect(Collectors.toList()));
    }

    @Override
    public void delete(Long id) {
        if (cityRepository.existsById(id)) {
            cityRepository.deleteById(id);
            log.info("City deleted by id: {}", id);
        } else {
            throw new ResourceNotFoundException("Not found city with id: " + id);
        }
    }
}

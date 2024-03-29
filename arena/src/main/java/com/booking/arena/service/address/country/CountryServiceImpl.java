package com.booking.arena.service.address.country;

import com.booking.arena.dto.address.CountryDto;
import com.booking.arena.entity.address.CountryEntity;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.repository.address.CountryRepository;
import com.booking.arena.utils.ConvertEntityToDto;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Country", description = "Country management APIs")
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    @Override
    public Optional<CountryDto> create(CountryDto countryDto) {
        try {
            CountryEntity country = CountryEntity.builder()
                    .name(countryDto.getName())
                    .build();
            country.setCreatedAt(Instant.now());
            country.setUpdatedAt(Instant.now());
            countryRepository.save(country);
            log.info("Country created: {}", country.getName());
            return Optional.of(CountryDto.builder()
                    .name(country.getName())
                    .build());
        } catch (Exception e) {
            log.error("Invalid country details\n" + e.getMessage());
            throw new ResourceNotFoundException("Invalid country details\n");
        }

    }

    @Override
    public Optional<CountryDto> update(Long id, CountryDto countryDto) {
        CountryEntity country = countryRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Not found country with id: " + id)
        );
        try {
            country.setName(countryDto.getName());
            country.setUpdatedAt(Instant.now());
            countryRepository.save(country);
            log.info("Country updated: {}", country);
            return Optional.of(CountryDto.builder()
                    .name(country.getName())
                    .build());
        } catch (Exception e) {
            log.error("Invalid country details\n" + e.getMessage());
            throw new ResourceNotFoundException("Invalid country details\n" + e.getMessage());
        }
    }

    @Override
    public Optional<CountryDto> getById(Long id) {
        CountryEntity country = countryRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Not found country with id: " + id)
        );
        return Optional.of(CountryDto.builder()
                .name(country.getName())
                .build());
    }

    @Override
    public Optional<CountryDto> getCountryByName(String name) {
        CountryEntity country = countryRepository.findByName(name).orElseThrow(() ->
                new ResourceNotFoundException("Not found country with id: " + name)
        );
        return Optional.of(CountryDto.builder()
                .name(country.getName())
                .build());
    }

    @Override
    public List<CountryDto> getAll() {
        return countryRepository.findAll().stream()
                .map(ConvertEntityToDto::countryToDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (!countryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Not found country with id: " + id);
        }
        countryRepository.deleteById(id);
        log.info("Country deleted with id: {}", id);
    }
}

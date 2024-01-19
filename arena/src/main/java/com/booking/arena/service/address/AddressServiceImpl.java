package com.booking.arena.service.address;

import com.booking.arena.dto.address.AddressDto;
import com.booking.arena.entity.address.Address;
import com.booking.arena.entity.address.CityEntity;
import com.booking.arena.entity.address.CountryEntity;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.repository.address.AddressRepository;
import com.booking.arena.repository.address.CityRepository;
import com.booking.arena.repository.address.CountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    @Override
    public Optional<Address> create(AddressDto addressDto) {
        CountryEntity country = countryRepository.findByName(addressDto.getCountry().getName()).orElseThrow(() ->
                new ResourceNotFoundException("No correspond to any country"));
        CityEntity city = cityRepository.findByName(addressDto.getCity().getName()).orElseThrow(() ->
                new ResourceNotFoundException("No correspond to any city"));
        Address address = new Address();
        try {
            address.setCountry(country);
            address.setCity(city);
            address.setStreet(addressDto.getStreet());
            address.setLongitude(addressDto.getLongitude());
            address.setLatitude(addressDto.getLatitude());
            address.setCreatedAt(Instant.now());
            address.setUpdatedAt(Instant.now());
            addressRepository.save(address);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException("Can't create address, check your input data");
        }
        return Optional.of(address);
    }

    @Override
    public Optional<Address> update(Long id, AddressDto addressDto) {
        Address address = addressRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not found address with id: " + id)
        );
        CountryEntity country = countryRepository.findByName(addressDto.getCountry().getName()).orElseThrow(
                () -> new ResourceNotFoundException("No correspond to any country")
        );
        CityEntity city = cityRepository.findByName(addressDto.getCity().getName()).orElseThrow(
                () -> new ResourceNotFoundException("No correspond to any city")
        );
        try {
            address.setCountry(country);
            address.setCity(city);
            address.setStreet(addressDto.getStreet());
            address.setUpdatedAt(Instant.now());
            address.setLongitude(addressDto.getLongitude());
            address.setLatitude(addressDto.getLatitude());
            addressRepository.save(address);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException("Can't update address, check your input data");
        }


        return Optional.of(address);
    }

    @Override
    public Optional<Address> getById(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not found address with id: " + id)
        );
        return Optional.of(address);
    }

    @Override
    public void delete(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not found address with id: " + id)
        );
        addressRepository.deleteById(address.getId());
    }
}

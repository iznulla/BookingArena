package com.booking.arena.service.address;


import com.booking.arena.dto.address.AddressDto;
import com.booking.arena.entity.address.Address;
import com.booking.arena.entity.address.CityEntity;
import com.booking.arena.entity.address.CountryEntity;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.repository.AddressRepository;
import com.booking.arena.repository.CityRepository;
import com.booking.arena.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    @Override
    public Optional<Address> createAddress(AddressDto addressDto) {
        CountryEntity country = countryRepository.findByName(addressDto.getCountry().getName()).orElseThrow(() ->
                new ResourceNotFoundException("No correspond to any country"));
        CityEntity city = cityRepository.findByName(addressDto.getCity().getName()).orElseThrow(() ->
                new ResourceNotFoundException("No correspond to any city"));

        Address address = new Address();
        address.setCountry(country);
        address.setCity(city);
        address.setStreet(addressDto.getStreet());
        address.setLongitude(addressDto.getLongitude());
        address.setLatitude(addressDto.getLatitude());
        address.setCreatedAt(Instant.now());
        address.setUpdatedAt(Instant.now());
        addressRepository.save(address);

        return Optional.of(address);
    }

    @Override
    public Optional<Address> updateAddress(Long id, AddressDto addressDto) {
        Address address = addressRepository.findById(id).orElseThrow();

        CountryEntity country = countryRepository.findByName(addressDto.getCountry().getName()).orElseThrow(
                () -> new ResourceNotFoundException("No correspond to any country")
        );
        CityEntity city = cityRepository.findByName(addressDto.getCity().getName()).orElseThrow(
                () -> new ResourceNotFoundException("No correspond to any city")
        );

        address.setCountry(country);
        address.setCity(city);
        address.setStreet(addressDto.getStreet());
        address.setUpdatedAt(Instant.now());
        address.setLongitude(addressDto.getLongitude());
        address.setLatitude(addressDto.getLatitude());
        addressRepository.save(address);

        return Optional.of(address);
    }

    @Override
    public Optional<Address> getAddressById(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not found address with id: " + id)
        );
        return Optional.of(address);
    }

    @Override
    public void deleteAddressById(Long id) {
        addressRepository.deleteById(id);
    }
}

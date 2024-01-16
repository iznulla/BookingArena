package com.booking.arena.service.address;

import com.booking.arena.dto.address.AddressDto;
import com.booking.arena.dto.address.CityDto;
import com.booking.arena.dto.address.CountryDto;
import com.booking.arena.entity.address.Address;
import com.booking.arena.entity.address.CityEntity;
import com.booking.arena.entity.address.CountryEntity;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.repository.address.AddressRepository;
import com.booking.arena.repository.address.CityRepository;
import com.booking.arena.repository.address.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {
    @Mock
    CityRepository cityRepository;
    @Mock
    CountryRepository countryRepository;
    @Mock
    AddressRepository addressRepository;

    @InjectMocks
    AddressServiceImpl addressService;

    /*
    Address utils data
     */
    AddressDto addressDto;
    AddressDto updatedAddressDto;
    Address address;
    Address updatedAddress;

    /*
    City utils data
     */
    CityDto cityDto;
    CityDto updatedCityDto;
    CityDto updatedCityCountryDto;
    CityEntity city;
    CityEntity updatedCity;

    /*
    Country utils data
     */
    CountryDto countryDto;
    CountryDto updatedCountryDto;
    CountryEntity country;
    CountryEntity updatedCountry;

    @BeforeEach
    void setUp() {
        countryDto = CountryDto.builder()
                .name("Country")
                .build();
        updatedCountryDto = CountryDto.builder()
                .name("Update")
                .build();
        country = CountryEntity.builder()
                .id(1L)
                .name("Country")
                .build();
        updatedCountry = CountryEntity.builder()
                .id(1L)
                .name("Update")
                .build();
        cityDto = CityDto.builder()
                .name("City")
                .countryName("Country")
                .build();
        city = CityEntity.builder()
                .id(1L)
                .name("City")
                .country(country)
                .build();
        updatedCity = CityEntity.builder()
                .id(2L)
                .name("Update")
                .country(updatedCountry)
                .build();
        updatedCityDto = CityDto.builder()
                .name("Update")
                .countryName("Country")
                .build();
        updatedCityCountryDto = CityDto.builder()
                .name("Update")
                .countryName("Update")
                .build();
        addressDto = AddressDto.builder()
                .city(cityDto)
                .country(countryDto)
                .latitude(1.0)
                .longitude(1.0)
                .street("street")
                .build();
        updatedAddressDto = AddressDto.builder()
                .city(updatedCityDto)
                .country(updatedCountryDto)
                .latitude(1.0)
                .longitude(1.0)
                .street("updated")
                .build();
        address = Address.builder()
                .id(1L)
                .street("street")
                .city(city)
                .country(country)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .latitude(1.0)
                .longitude(1.0)
                .build();

       updatedAddress = Address.builder()
               .id(1L)
               .street("updated")
               .city(updatedCity)
               .createdAt(Instant.now())
               .updatedAt(Instant.now())
               .latitude(1.0)
               .longitude(1.0)
               .country(updatedCountry)
               .build();
    }

    @Test
    void AddressService_createAddress_returnAddressEntity() {
        when(countryRepository.findByName(any())).thenReturn(Optional.of(country));
        when(cityRepository.findByName(any())).thenReturn(Optional.of(city));
        Address addressTest = addressService.create(addressDto).orElseThrow();
        assertAll(
                () -> assertThat(addressTest).isNotNull(),
                () -> assertThat(addressTest.getStreet()).isEqualTo(address.getStreet()),
                () -> assertThat(addressTest.getCountry().getName()).isEqualTo(address.getCountry().getName()),
                () -> assertThat(addressTest.getCity().getName()).isEqualTo(address.getCity().getName()),
                () -> assertThat(addressTest.getLatitude()).isEqualTo(address.getLatitude()),
                () -> assertThat(addressTest.getLongitude()).isEqualTo(address.getLongitude())
        );
    }

    @Test
    void AddressService_updateAddress_returnAddressEntity() {
        when(countryRepository.findByName(any())).thenReturn(Optional.of(updatedCountry));
        when(cityRepository.findByName(any())).thenReturn(Optional.of(updatedCity));
        when(addressRepository.findById(any())).thenReturn(Optional.of(address));
        Address addressTest = addressService.update(1L, updatedAddressDto).orElseThrow();
        assertAll(
                () -> assertThat(addressTest).isNotNull(),
                () -> assertThat(addressTest.getStreet()).isEqualTo(updatedAddress.getStreet()),
                () -> assertThat(addressTest.getCountry().getName()).isEqualTo(updatedAddress.getCountry().getName()),
                () -> assertThat(addressTest.getCity().getName()).isEqualTo(updatedAddress.getCity().getName()),
                () -> assertThat(addressTest.getLatitude()).isEqualTo(updatedAddress.getLatitude()),
                () -> assertThat(addressTest.getLongitude()).isEqualTo(updatedAddress.getLongitude())
        );
    }

    @Test
    void AddressService_getByIdAddress_returnAddressEntity() {
        when(addressRepository.findById(any())).thenReturn(Optional.of(address));
        Address addressTest = addressService.getById(1L).orElseThrow();
        verify(addressRepository).findById(eq(1L));
        assertAll(
                () -> assertThat(addressTest).isNotNull(),
                () -> assertThat(addressTest.getStreet()).isEqualTo(address.getStreet()),
                () -> assertThat(addressTest.getCountry().getName()).isEqualTo(address.getCountry().getName()),
                () -> assertThat(addressTest.getCity().getName()).isEqualTo(address.getCity().getName()),
                () -> assertThat(addressTest.getLatitude()).isEqualTo(address.getLatitude()),
                () -> assertThat(addressTest.getLongitude()).isEqualTo(address.getLongitude())
        );
    }

//    @Test
//    void CountryService_getAll_returnListCountryDto() {
//        when(cityRepository.findAll()).thenReturn(List.of(city));
//        List<CityDto> cityDtoTest = addressService.getAll().orElseThrow();
//        verify(cityRepository).findAll();
//        assertAll(
//                () -> assertThat(cityDtoTest).isNotNull()
//        );
//    }

    @Test
    void AddressService_delete_returnNon() {
        when(addressRepository.findById(any())).thenReturn(Optional.of(address));
        assertAll(
                () -> addressService.delete(1L)
        );
    }

    @Test
    void AddressService_checkAllMethodsOnThrows_returnThrows() {
        assertAll(
                () -> assertThrows(ResourceNotFoundException.class, () -> addressService.update(1L, updatedAddressDto)),
                () -> assertThrows(ResourceNotFoundException.class, () -> addressService.getById(1L)),
                () -> assertThrows(ResourceNotFoundException.class, () -> addressService.delete(1L))
        );
    }
}

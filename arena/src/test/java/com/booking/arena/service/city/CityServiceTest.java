package com.booking.arena.service.city;

import com.booking.arena.dto.address.CityDto;
import com.booking.arena.dto.address.CountryDto;
import com.booking.arena.entity.address.CityEntity;
import com.booking.arena.entity.address.CountryEntity;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.repository.address.CityRepository;
import com.booking.arena.repository.address.CountryRepository;
import com.booking.arena.service.address.city.CityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CityServiceTest {
    @Mock
    CityRepository cityRepository;
    @Mock
    CountryRepository countryRepository;

    @InjectMocks
    CityServiceImpl cityService;

    /*
    City utils data
     */
    CityDto cityDto;
    CityDto updatedCityDto;
    CityDto updatedCityCountryDto;
    CityEntity city;

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
        updatedCityDto = CityDto.builder()
                .name("Update")
                .countryName("Country")
                .build();
        updatedCityCountryDto = CityDto.builder()
                .name("Update")
                .countryName("Update")
                .build();


    }

    @Test
    void CityService_createCity_returnCityDto() {
        when(countryRepository.findByName(any())).thenReturn(Optional.of(country));
        CityDto cityTestDto = cityService.create(cityDto).orElseThrow();
        assertAll(
                () -> assertThat(cityTestDto).isNotNull(),
                () -> assertThat(cityTestDto.getName()).isEqualTo(cityDto.getName()),
                () -> assertThat(cityTestDto.getCountryName()).isEqualTo(cityDto.getCountryName())
        );
    }

    @Test
    void CityService_updateCity_returnCityDto() {
        when(cityRepository.findById(any())).thenReturn(Optional.of(city));
        CityDto cityDtoTest = cityService.update(1L, updatedCityDto).orElseThrow();
        verify(cityRepository).findById(eq(1L));
        assertAll(
                () -> assertThat(cityDtoTest).isNotNull(),
                () -> assertThat(cityDtoTest.getName()).isEqualTo(city.getName()),
                () -> assertThat(cityDtoTest.getCountryName()).isEqualTo(city.getCountry().getName())
        );
        when(cityRepository.findById(any())).thenReturn(Optional.of(city));
        CityDto cityDtoTestUpdate = cityService.update(1L, updatedCityCountryDto).orElseThrow();
        assertAll(
                () -> assertThat(cityDtoTestUpdate).isNotNull(),
                () -> assertThat(cityDtoTestUpdate.getName()).isEqualTo(city.getName()),
                () -> assertThat(cityDtoTestUpdate.getCountryName()).isEqualTo(city.getCountry().getName())
        );
    }

    @Test
    void CityService_getByIdCity_returnCityDto() {
        when(cityRepository.findById(any())).thenReturn(Optional.of(city));
        CityDto cityDtoTest = cityService.getById(1L).orElseThrow();
        verify(cityRepository).findById(eq(1L));
        assertAll(
                () -> assertThat(cityDtoTest).isNotNull(),
                () -> assertThat(cityDtoTest.getName()).isEqualTo(cityDto.getName()),
                () -> assertThat(cityDtoTest.getCountryName()).isEqualTo(city.getCountry().getName())
        );
    }

    @Test
    void CItyService_getByNameCIty_returnCItyDto() {
        when(cityRepository.findByName(any())).thenReturn(Optional.of(city));
        CityDto cityDtoTest = cityService.getCityByName("City").orElseThrow();
        verify(cityRepository).findByName(eq("City"));
        assertAll(
                () -> assertThat(cityDtoTest).isNotNull(),
                () -> assertThat(cityDtoTest.getName()).isEqualTo(city.getName()),
                () -> assertThat(cityDtoTest.getCountryName()).isEqualTo(city.getCountry().getName())
        );
    }

    @Test
    void CityService_getAll_returnListCityDto() {
        when(cityRepository.findAll()).thenReturn(List.of(city));
        List<CityDto> cityDtoTest = cityService.getAll().orElseThrow();
        verify(cityRepository).findAll();
        assertAll(
                () -> assertThat(cityDtoTest).isNotNull()
        );
    }

    @Test
    void CityService_delete_returnNon() {
        when(cityRepository.existsById((any()))).thenReturn(true);
        assertAll(
                () -> cityService.delete(1L)
        );
    }

    @Test
    void CityService_checkAllMethodsOnThrows_returnThrows() {
        assertAll(
                () -> assertThrows(ResourceNotFoundException.class, () -> cityService.update(1L, updatedCityDto)),
                () -> assertThrows(ResourceNotFoundException.class, () -> cityService.getById(1L)),
                () -> assertThrows(ResourceNotFoundException.class, () -> cityService.getCityByName("Country")),
                () -> assertThrows(ResourceNotFoundException.class, () -> cityService.delete(1L))
        );
    }
}

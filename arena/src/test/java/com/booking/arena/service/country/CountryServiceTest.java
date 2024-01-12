package com.booking.arena.service.country;

import com.booking.arena.dto.address.CountryDto;
import com.booking.arena.entity.address.CountryEntity;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.repository.address.CountryRepository;
import com.booking.arena.service.address.country.CountryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CountryServiceTest {
    @Mock
    CountryRepository countryRepository;

    @InjectMocks
    CountryServiceImpl countryService;

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
    }

    @Test
    void CountryService_createCountry_returnCountryDto() {
        when(countryRepository.save(any())).thenReturn(any());
        CountryDto country = countryService.create(countryDto).orElseThrow();
        assertAll(
                () -> assertThat(country).isNotNull(),
                () -> assertThat(country.getName()).isEqualTo(country.getName())
        );
    }

    @Test
    void CountryService_updateCountry_returnCountryDto() {
        when(countryRepository.findById(any())).thenReturn(Optional.of(country));
        CountryDto countryDto1 = countryService.update(1L, updatedCountryDto).orElseThrow();
        verify(countryRepository).findById(eq(1L));
        assertAll(
                () -> assertThat(countryDto1).isNotNull(),
                () -> assertThat(countryDto1.getName()).isEqualTo(updatedCountryDto.getName())
        );
    }

    @Test
    void CountryService_getByIdCountry_returnCountryDto() {
        when(countryRepository.findById(any())).thenReturn(Optional.of(country));
        CountryDto countryDtoTest = countryService.getById(1L).orElseThrow();
        verify(countryRepository).findById(eq(1L));
        assertAll(
                () -> assertThat(countryDtoTest).isNotNull(),
                () -> assertThat(countryDtoTest.getName()).isEqualTo(countryDto.getName())
        );
    }

    @Test
    void CountryService_getByNameCountry_returnCountryDto() {
        when(countryRepository.findByName(any())).thenReturn(Optional.of(country));
        CountryDto countryDtoTest = countryService.getCountryByName("Country").orElseThrow();
        verify(countryRepository).findByName(eq("Country"));
        assertAll(
                () -> assertThat(countryDtoTest).isNotNull(),
                () -> assertThat(countryDtoTest.getName()).isEqualTo(countryDto.getName())
        );
    }

    @Test
    void CountryService_getAll_returnListCountryDto() {
        when(countryRepository.findAll()).thenReturn(List.of(country));
        List<CountryDto> countryDtoTest = countryService.getAll();
        verify(countryRepository).findAll();
        assertAll(
                () -> assertThat(countryDtoTest).isNotNull()
        );
    }

    @Test
    void CountryService_delete_returnNon() {
        when(countryRepository.existsById((any()))).thenReturn(true);
        assertAll(
                () -> countryService.delete(1L)
        );
    }

    @Test
    void CountryService_checkAllMethodsOnThrows_returnThrows() {
        assertAll(
                () -> assertThrows(ResourceNotFoundException.class, () -> countryService.update(1L, updatedCountryDto)),
                () -> assertThrows(ResourceNotFoundException.class, () -> countryService.getById(1L)),
                () -> assertThrows(ResourceNotFoundException.class, () -> countryService.getCountryByName("Country")),
                () -> assertThrows(ResourceNotFoundException.class, () -> countryService.delete(1L))
        );
    }
}

package com.booking.arena.service.booking;

import com.booking.arena.dto.address.AddressDto;
import com.booking.arena.dto.address.CityDto;
import com.booking.arena.dto.address.CountryDto;
import com.booking.arena.dto.arena.ArenaDto;
import com.booking.arena.dto.booking.ReservationArenaDto;
import com.booking.arena.dto.user.*;
import com.booking.arena.dto.user.update.UserUpdateDto;
import com.booking.arena.entity.address.Address;
import com.booking.arena.entity.address.CityEntity;
import com.booking.arena.entity.address.CountryEntity;
import com.booking.arena.entity.arena.ArenaEntity;
import com.booking.arena.entity.arena.ArenaInfo;
import com.booking.arena.entity.booking.BookingUser;
import com.booking.arena.entity.booking.ReservationArena;
import com.booking.arena.entity.user.*;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.repository.arena.ArenaRepository;
import com.booking.arena.repository.booking.ReservationArenaRepository;
import com.booking.arena.repository.user.UserRepository;
import com.booking.arena.security.UserPrincipal;
import com.booking.arena.service.arena.ArenaInfoService;
import com.booking.arena.utils.ConvertEntityToDto;
import com.booking.arena.utils.SecurityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {
    @Mock
    ArenaRepository arenaRepository;

    @Mock
    UserRepository userRepository;
    @Mock
    ReservationArenaRepository reservationArenaRepository;

    @InjectMocks
    ReservationServiceImpl reservationService;

    /*
    Role data
     */
    RoleEntity role;
    RoleEntity updatedRole;

    RoleDto roleDto;
    RoleDto updatedRoleDto;

    /*
    Privilege data
     */
    PrivilegeDto privilegeDto;
    PrivilegeDto updatedPrivilegeDto;
    Privilege privilege;
    Privilege updatedPrivilege;

    /*
    RolePrivilege data
     */

    RolePrivilege rolePrivilege;
    RolePrivilegeDto rolePrivilegeDto;
    RolePrivilege updatedRolePrivilege;

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

    /*
    User utils data
     */
    UserEntity user;
    UserEntity updatedUser;
    UserDto userDto;
    UserUpdateDto updatedUserDto;
    UserProfile userProfile;
    UserProfileDto userProfileDto;

    /*
    Arena utils data
     */
    ArenaDto arenaDto;
    ArenaEntity arena;
    ArenaInfo arenaInfo;
    ArenaEntity updateArena;
    ArenaInfo updatedArenaInfo;

    /*
    UserPrincipal
     */
    UserPrincipal userPrincipal;

    /*
    Booking utils
     */
    ReservationArenaDto reservationArenaDto;
    ReservationArena reservationArena;

    ReservationArenaDto updatedReservationArenaDto;
    ReservationArena updatedReservationArena;

    @BeforeEach
    void setUp() {
        userPrincipal = UserPrincipal.builder()
                .userId(1L)
                .build();
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
                .name("Uzbekistan")
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
                .name("Tashkent")
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
                .street("Maksim Gorkiy")
                .city(updatedCity)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .latitude(1.0)
                .longitude(1.0)
                .country(updatedCountry)
                .build();
        privilegeDto = PrivilegeDto.builder()
                .name("READ")
                .build();
        updatedPrivilegeDto = PrivilegeDto.builder()
                .name("Update")
                .build();
        privilege = Privilege.builder()
                .id(1L)
                .name("READ")
                .build();
        updatedPrivilege = Privilege.builder()
                .id(2L)
                .name("Update")
                .build();

        role = RoleEntity.builder()
                .id(1L)
                .name("ADMIN")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        updatedRole = RoleEntity.builder()
                .id(1L)
                .name("UPDATE")
                .updatedAt(Instant.now())
                .build();
        updatedRolePrivilege = RolePrivilege.builder()
                .privilege(updatedPrivilege)
                .build();
        rolePrivilege = RolePrivilege.builder()
                .id(1L)
                .privilege(privilege)
                .role(role)
                .build();
        rolePrivilegeDto = RolePrivilegeDto.builder()
                .privilege("ADMIN")
                .build();
        roleDto = RoleDto.builder()
                .name("ADMIN")
                .rolePrivileges(List.of(rolePrivilegeDto))
                .build();
        updatedRoleDto = RoleDto.builder()
                .name("UPDATE")
                .build();

        role.setRolePrivileges(List.of(rolePrivilege));

        user = UserEntity.builder()
                .email("email")
                .role(role)
                .userId(1L)
                .username("username")
                .isActive(true)
                .build();
        updatedUser = UserEntity.builder()
                .email("updateEmail")
                .role(updatedRole)
                .userId(1L)
                .username("updateUsername")
                .isActive(true)
                .build();
        updatedUserDto = UserUpdateDto.builder()
                .email("updateEmail")
                .role("UPDATE")
                .username("updateUsername")
                .build();
        userProfile = UserProfile.builder()
                .id(1L)
                .user(user)
                .name("Name")
                .surname("Surname")
                .address(address)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        user.setUserProfile(userProfile);
        userProfileDto = UserProfileDto.builder()
                .name("Name")
                .surname("Surname")
                .address(addressDto)
                .build();
        userDto = UserDto.builder()
                .email("email")
                .role(roleDto)
                .username("username")
                .userProfile(userProfileDto)
                .build();

        arenaInfo = ArenaInfo.builder()
                .id(1L)
                .address(address)
                .phone("+998339191919")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .price(125000)
                .workedFrom(Instant.now())
                .workedTo(Instant.now())
                .build();

        updatedArenaInfo = ArenaInfo.builder()
                .id(1L)
                .address(updatedAddress)
                .phone("+998336161616")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .workedFrom(Instant.now())
                .workedTo(Instant.now())
                .price(120000)
                .build();

        arenaDto = ArenaDto.builder()
                .name("Budapest Arena")
                .description("World's best arena")
                .status(true)
                .build();
        arena = ArenaEntity.builder()
                .id(1L)
                .name("Budapest Arena")
                .description("World's best arena")
                .image("test.txt")
                .arenaInfo(arenaInfo)
                .build();
        updateArena = ArenaEntity.builder()
                .id(1L)
                .name("Allianz Arena")
                .description("World's true arena")
                .arenaInfo(updatedArenaInfo)
                .build();
//        BookingUser bookingUser = BookingUser.builder()
//                .user(user)
//                .reservationArena(reservationArena)
//                .build();
        reservationArena = ReservationArena.builder()
                .id(1L)
                .arena(arena)
                .description("test")
                .createdAt(Instant.now())
                .costumer("user")
                .totalPrice(250000)
                .bookingFrom(Instant.parse("2024-01-16T06:12:17.464076Z"))
                .bookingTo(Instant.parse("2024-01-16T08:12:17.464076Z"))
                .build();
        reservationArenaDto = ReservationArenaDto.builder()
                .arenaId(1L)
                .description("test")
                .createdAt(Instant.now())
                .costumer("user")
                .totalPrice(250000)
                .bookingFrom(Instant.parse("2024-01-16T06:12:17.464076Z"))
                .bookingTo(Instant.parse("2024-01-16T08:12:17.464076Z"))
                .build();
        updatedReservationArena = ReservationArena.builder()
                .id(2L)
                .arena(arena)
                .description("updated")
                .createdAt(Instant.now())
                .costumer("updated")
                .totalPrice(125000)
                .bookingFrom(Instant.parse("2024-01-17T06:12:17.464076Z"))
                .bookingTo(Instant.parse("2024-01-17T07:12:17.464076Z"))
                .build();
        updatedReservationArenaDto = ReservationArenaDto.builder()
                .arenaId(2L)
                .description("updated")
                .createdAt(Instant.now())
                .costumer("updated")
                .totalPrice(125000)
                .bookingFrom(Instant.parse("2024-01-17T06:12:17.464076Z"))
                .bookingTo(Instant.parse("2024-01-17T07:12:17.464076Z"))
                .build();
    }

    @Test
    void ReservationService_createReservation_returnReservationDto() {
        try (MockedStatic<SecurityUtils> utilities = Mockito.mockStatic(SecurityUtils.class)) {
            utilities.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(userRepository.findById(any())).thenReturn(Optional.of(user));
            when(arenaRepository.findById(any())).thenReturn(Optional.of(arena));
            ReservationArenaDto reservArenaTest = reservationService.create(reservationArenaDto).orElseThrow();
            assertAll(
                    () -> assertThat(reservArenaTest).isNotNull(),
                    () -> Assertions.assertEquals(reservArenaTest, ConvertEntityToDto.reservationArenaToDto(reservationArena))
            );
        }
    }

    @Test
    void ReservationService_updateReservation_returnReservationDto() {
        try (MockedStatic<SecurityUtils> utilities = Mockito.mockStatic(SecurityUtils.class)) {
            utilities.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(arenaRepository.findById(any())).thenReturn(Optional.of(updateArena));
            when(reservationArenaRepository.findById(any())).thenReturn(Optional.of(updatedReservationArena));
            ReservationArenaDto reservArenaTest = reservationService.update(1L, updatedReservationArenaDto).orElseThrow();
            assertAll(
                    () -> assertThat(reservArenaTest).isNotNull(),
                    () -> Assertions.assertEquals(reservArenaTest, ConvertEntityToDto.reservationArenaToDto(updatedReservationArena))
            );
        }
    }

    @Test
    void ReservationService_getByIdReservation_returnReservationDto() {
        when(reservationArenaRepository.findById(1L)).thenReturn(Optional.of(reservationArena));
        ReservationArenaDto reservArenaTest = reservationService.getById(1L).orElseThrow();
        assertAll(
                () -> assertThat(reservArenaTest).isNotNull(),
                () -> Assertions.assertEquals(reservArenaTest, ConvertEntityToDto.reservationArenaToDto(reservationArena))
        );
    }

    @Test
    void ReservationService_getAll_returnListReservationDto() {
        when(reservationArenaRepository.findAll()).thenReturn(List.of(reservationArena));
        List<ReservationArenaDto> reservArenaTest = reservationService.getAll();
        verify(reservationArenaRepository).findAll();
        assertAll(
                () -> assertThat(reservArenaTest).isNotNull()
        );
    }

    @Test
    void ReservationService_delete_returnNon() {
        try (MockedStatic<SecurityUtils> utilities = Mockito.mockStatic(SecurityUtils.class)) {
            utilities.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(reservationArenaRepository.findById(any())).thenReturn(Optional.of(reservationArena));
            assertAll(
                    () -> reservationService.delete(1L)
            );
        }
    }

    @Test
    void ReservationService_checkAllMethodsOnThrows_returnThrows() {
        assertAll(
                () -> assertThrows(ResourceNotFoundException.class, () -> reservationService.update(1L, any())),
                () -> assertThrows(ResourceNotFoundException.class, () -> reservationService.getById(1L)),
                () -> assertThrows(ResourceNotFoundException.class, () -> reservationService.delete(1L))
        );
    }
}

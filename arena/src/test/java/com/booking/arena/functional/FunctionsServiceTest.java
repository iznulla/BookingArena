package com.booking.arena.functional;

import com.booking.arena.dto.address.AddressDto;
import com.booking.arena.dto.address.CityDto;
import com.booking.arena.dto.address.CountryDto;
import com.booking.arena.dto.arena.ArenaDto;
import com.booking.arena.dto.arena.ArenaFiltersDto;
import com.booking.arena.dto.booking.ReservationArenaDto;
import com.booking.arena.dto.user.*;
import com.booking.arena.entity.address.Address;
import com.booking.arena.entity.address.CityEntity;
import com.booking.arena.entity.address.CountryEntity;
import com.booking.arena.entity.arena.ArenaEntity;
import com.booking.arena.entity.arena.ArenaInfo;
import com.booking.arena.entity.booking.ReservationArena;
import com.booking.arena.entity.user.*;
import com.booking.arena.repository.arena.ArenaRepository;
import com.booking.arena.repository.booking.ReservationArenaRepository;
import com.booking.arena.repository.user.UserRepository;
import com.booking.arena.security.UserPrincipal;
import com.booking.arena.service.arena.ArenaServiceImpl;
import com.booking.arena.service.booking.ReservationServiceImpl;
import com.booking.arena.utils.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FunctionsServiceTest {
    @Mock
    ArenaRepository arenaRepository;

    @Mock
    UserRepository userRepository;
    @Mock
    ReservationArenaRepository reservationArenaRepository;

    @InjectMocks
    ReservationServiceImpl reservationService;

    @InjectMocks
    ArenaServiceImpl arenaService;

    /*
    Role data
     */
    RoleEntity role;

    RoleDto roleDto;

    /*
    Privilege data
     */
    PrivilegeDto privilegeDto;
    Privilege privilege;

    /*
    RolePrivilege data
     */

    RolePrivilege rolePrivilege;
    RolePrivilegeDto rolePrivilegeDto;

    /*
    Address utils data
     */
    AddressDto addressDto;
    AddressDto updatedAddressDto;
    AddressDto furtherAddressDto;
    AddressDto nearAddressDto;
    Address address;
    Address furtherAddress;
    Address nearAddress;
    Address updatedAddress;

    /*
    City utils data
     */
    CityDto cityDto;
    CityEntity city;

    /*
    Country utils data
     */
    CountryDto countryDto;
    CountryEntity country;

    /*
    User utils data
     */
    UserEntity user;
    UserDto userDto;
    UserProfile userProfile;
    UserProfileDto userProfileDto;

    /*
    Arena utils data
     */
    ArenaDto arenaDto;
    ArenaEntity nearArena;
    ArenaInfo arenaInfo;
    ArenaEntity furtherArena;
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
                .name("Uzbekistan")
                .build();
        country = CountryEntity.builder()
                .id(1L)
                .name("Uzbekistan")
                .build();
        cityDto = CityDto.builder()
                .name("Tashkent")
                .countryName("Uzbekistan")
                .build();
        city = CityEntity.builder()
                .id(1L)
                .name("Tashkent")
                .country(country)
                .build();
        CityEntity samarkand =
                CityEntity.builder()
                        .name("Samarkand")
                        .country(country)
                        .build();

        address = Address.builder().street("IT Park")
                .city(city)
                .country(country)
                .latitude(41.34101239392938)
                .longitude(69.33599271527646)
                .build();

        furtherAddress = Address.builder()
                .street("Yuzrabot")
                .city(samarkand)
                .country(country)
                .latitude(41.35748363693251)
                .longitude(69.37662189241189)
                .build();

        nearAddress = Address.builder()
                .street("Ekobazar")
                .city(city)
                .country(country)
                .latitude(41.34860798652945)
                .longitude(69.35792002223815)
                .build();

        privilegeDto = PrivilegeDto.builder()
                .name("READ")
                .build();
        privilege = Privilege.builder()
                .id(1L)
                .name("READ")
                .build();

        role = RoleEntity.builder()
                .id(1L)
                .name("ADMIN")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
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

        role.setRolePrivileges(List.of(rolePrivilege));

        user = UserEntity.builder()
                .email("email")
                .role(role)
                .userId(1L)
                .username("username")
                .isActive(true)
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
                .address(nearAddress)
                .phone("+998339191919")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .price(125000)
                .workedFrom(Instant.now())
                .workedTo(Instant.now())
                .build();

        updatedArenaInfo = ArenaInfo.builder()
                .id(1L)
                .address(furtherAddress)
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
        nearArena = ArenaEntity.builder()
                .id(1L)
                .name("Budapest Arena")
                .description("World's best arena")
                .image("test.txt")
                .arenaInfo(arenaInfo)
                .build();
        furtherArena = ArenaEntity.builder()
                .id(1L)
                .name("Allianz Arena")
                .description("World's true arena")
                .arenaInfo(updatedArenaInfo)
                .build();
        reservationArena = ReservationArena.builder()
                .id(1L)
                .arena(nearArena)
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
                .arena(nearArena)
                .description("updated")
                .createdAt(Instant.now())
                .costumer("updated")
                .totalPrice(360000)
                .bookingFrom(Instant.parse("2024-01-17T06:12:17.464076Z"))
                .bookingTo(Instant.parse("2024-01-17T09:00:00.464076Z"))
                .build();
        updatedReservationArenaDto = ReservationArenaDto.builder()
                .arenaId(2L)
                .description("updated")
                .createdAt(Instant.now())
                .costumer("updated")
                .totalPrice(360000)
                .bookingFrom(Instant.parse("2024-01-17T06:12:17.464076Z"))
                .bookingTo(Instant.parse("2024-01-17T09:00:00.464076Z"))
                .build();
    }

    @Test
    void ReservationService_createCreateFunction_returnReservationDto() {
        try (MockedStatic<SecurityUtils> utilities = Mockito.mockStatic(SecurityUtils.class)) {
            utilities.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(userRepository.findById(any())).thenReturn(Optional.of(user));
            when(arenaRepository.findById(any())).thenReturn(Optional.of(nearArena));
            ReservationArenaDto reservArenaTest = reservationService.create(reservationArenaDto).orElseThrow();
            assertAll(
                    () -> assertThat(reservArenaTest).isNotNull()
            );
        }
    }

    @Test
    void ReservationService_checkTotalPrice() {
        try (MockedStatic<SecurityUtils> utilities = Mockito.mockStatic(SecurityUtils.class)) {
            utilities.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(userRepository.findById(any())).thenReturn(Optional.of(user));
            when(arenaRepository.findById(any())).thenReturn(Optional.of(nearArena));
            ReservationArenaDto reservArenaTest = reservationService.create(reservationArenaDto).orElseThrow();
            assertAll(
                    () -> assertThat(reservArenaTest).isNotNull(),
                    () -> assertThat(reservArenaTest.getTotalPrice()).isEqualTo(250000)
            );
            nearArena.getArenaInfo().setPrice(200000);
            assertThat(reservationService.create(reservationArenaDto).orElseThrow()).isNotEqualTo(400000);
            assertThat(reservationService.create(reservationArenaDto).orElseThrow()).isNotEqualTo(reservationArena.getTotalPrice());
            assertThat(reservationService.create(updatedReservationArenaDto).orElseThrow()).isNotEqualTo(360000);
            assertThat(reservationService.create(updatedReservationArenaDto).orElseThrow()).isNotEqualTo(updatedReservationArena.getTotalPrice());
        }
    }

    @Test
    void ReservationService_checkReservationInformation() {
        try (MockedStatic<SecurityUtils> utilities = Mockito.mockStatic(SecurityUtils.class)) {
            utilities.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(userRepository.findById(any())).thenReturn(Optional.of(user));
            when(arenaRepository.findById(1L)).thenReturn(Optional.of(nearArena));
            ReservationArenaDto reservArenaTest = reservationService.create(reservationArenaDto).orElseThrow();
            assertAll(
                    () -> assertThat(reservArenaTest).isNotNull(),
                    () -> assertThat(reservArenaTest.getArenaId()).isEqualTo(1L),
                    () -> assertThat(reservArenaTest.getCostumer()).isEqualTo("user"),
                    () -> assertThat(reservArenaTest.getBookingFrom()).isEqualTo(Instant.parse("2024-01-16T06:12:17.464076Z")),
                    () -> assertThat(reservArenaTest.getBookingTo()).isEqualTo(Instant.parse("2024-01-16T08:12:17.464076Z")),
                    () -> assertThat(reservArenaTest.getDescription()).isEqualTo("test"),
                    () -> assertThat(reservArenaTest.getTotalPrice()).isEqualTo(250000)
            );
            when(arenaRepository.findById(2L)).thenReturn(Optional.of(furtherArena));
            ReservationArenaDto updatedReservArenaTest = reservationService.create(updatedReservationArenaDto).orElseThrow();
            assertAll(
                    () -> assertThat(updatedReservArenaTest).isNotNull(),
                    () -> assertThat(updatedReservArenaTest.getCostumer()).isEqualTo("updated"),
                    () -> assertThat(updatedReservArenaTest.getBookingFrom()).isEqualTo(Instant.parse("2024-01-17T06:12:17.464076Z")),
                    () -> assertThat(updatedReservArenaTest.getBookingTo()).isEqualTo(Instant.parse("2024-01-17T09:00:00.464076Z")),
                    () -> assertThat(updatedReservArenaTest.getDescription()).isEqualTo("updated"),
                    () -> assertThat(updatedReservArenaTest.getTotalPrice()).isEqualTo(360000)

            );
        }
    }

    @Test
    void ArenaService_checkArenaGetByFilterByTimeFromTo() {
        try (MockedStatic<SecurityUtils> utilities = Mockito.mockStatic(SecurityUtils.class)) {
            utilities.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(arenaRepository.findAll()).thenReturn(List.of(nearArena, furtherArena));
            List<ArenaDto> arenaList = arenaService.getByFilter(
                    ArenaFiltersDto.builder()
                            .from(Instant.parse("2024-01-16T06:12:17.464076Z"))
                            .to(Instant.parse("2024-01-16T08:12:17.464076Z"))
                            .build()
            );
            assertThat(arenaList).isNotEmpty();
            assertThat(arenaList).hasSize(2);
        }
    }

    @Test
    void ArenaService_checkArenaGetByFilterByLocation() {
        try (MockedStatic<SecurityUtils> utilities = Mockito.mockStatic(SecurityUtils.class)) {
            utilities.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(arenaRepository.findAll()).thenReturn(List.of(nearArena, furtherArena));
            List<ArenaDto> arenaList = arenaService.getByFilter(
                    ArenaFiltersDto.builder()
                            .longitude(userProfile.getAddress().getLongitude())
                            .latitude(userProfile.getAddress().getLatitude())
                            .build()
            );
            assertThat(arenaList).isNotEmpty();
            assertThat(arenaList).hasSize(2);
            assertThat(arenaList.get(0).getName()).isEqualTo(nearArena.getName());
        }
    }

    @Test
    void ArenaService_checkArenaGetByFilterByCity() {
        try (MockedStatic<SecurityUtils> utilities = Mockito.mockStatic(SecurityUtils.class)) {
            utilities.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(arenaRepository.findAll()).thenReturn(List.of(nearArena, furtherArena));
            List<ArenaDto> arenaList = arenaService.getByFilter(
                    ArenaFiltersDto.builder()
                            .city("Samarkand")
                            .build()
            );
            assertThat(arenaList).isNotEmpty();
            assertThat(arenaList).hasSize(1);
            assertThat(arenaList.get(0).getArenaInfo().getAddress().getCity().getName()).isEqualTo("Samarkand");
            assertThat(arenaList.get(0).getArenaInfo().getPhone()).isEqualTo("+998336161616");
            assertThat(arenaList.get(0).getArenaInfo().getPrice()).isEqualTo(120000);

            List<ArenaDto> arenaListTas = arenaService.getByFilter(
                    ArenaFiltersDto.builder()
                            .city("Tashkent")
                            .build()
            );
            assertThat(arenaListTas).isNotEmpty();
            assertThat(arenaListTas).hasSize(1);
            assertThat(arenaListTas.get(0).getArenaInfo().getAddress().getCity().getName()).isEqualTo("Tashkent");
            assertThat(arenaListTas.get(0).getArenaInfo().getPhone()).isEqualTo("+998339191919");
            assertThat(arenaListTas.get(0).getArenaInfo().getPrice()).isEqualTo(125000);

            List<ArenaDto> arenaLisEmpty = arenaService.getByFilter(
                    ArenaFiltersDto.builder()
                            .city("Test")
                            .build()
            );
            assertThat(arenaLisEmpty).isEmpty();
        }
    }


}

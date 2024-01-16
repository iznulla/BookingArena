package com.booking.arena.utils;

import com.booking.arena.dto.address.AddressDto;
import com.booking.arena.dto.address.CityDto;
import com.booking.arena.dto.address.CountryDto;
import com.booking.arena.dto.arena.ArenaDto;
import com.booking.arena.dto.arena.ArenaInfoDto;
import com.booking.arena.dto.auth.SignUpDto;
import com.booking.arena.dto.booking.ReservationArenaDto;
import com.booking.arena.dto.user.*;
import com.booking.arena.entity.address.Address;
import com.booking.arena.entity.address.CityEntity;
import com.booking.arena.entity.address.CountryEntity;
import com.booking.arena.entity.arena.ArenaEntity;
import com.booking.arena.entity.booking.ReservationArena;
import com.booking.arena.entity.user.*;

import java.util.stream.Collectors;

public class ConvertEntityToDto {
    public static CityDto cityToDto(CityEntity city) {
        return CityDto.builder()
                .id(city.getId())
                .name(city.getName())
                .countryName(city.getCountry().getName())
                .build();
    }

    public static CountryDto countryToDto(CountryEntity country) {
        return CountryDto.builder()
                .id(country.getId())
                .name(country.getName())
                .build();
    }

    public static AddressDto addressToDto(Address address) {
        return AddressDto.builder()
                .street(address.getStreet())
                .longitude(address.getLongitude())
                .latitude(address.getLatitude())
                .country(countryToDto(address.getCountry()))
                .city(cityToDto(address.getCity()))
                .build();
    }
    public static SignUpDto signUpToDto(UserEntity user) {
        return SignUpDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .userProfile(UserProfileDto.builder()
                        .name(user.getUserProfile().getName())
                        .surname(user.getUserProfile().getSurname())
                        .address(addressToDto(user.getUserProfile().getAddress()))
                        .build())
                .build();
    }

    public static UserDto userToDto(UserEntity user) {
        return UserDto.builder()
                .id(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(roleToDto(user.getRole()))
                .isActive(user.isActive())
                .userProfile(userProfileToDto(user.getUserProfile()))
                .build();
    }

    public static UserProfileDto userProfileToDto(UserProfile userProfile) {
        return UserProfileDto.builder()
                .id(userProfile.getId())
                .username(userProfile.getUser().getUsername())
                .email(userProfile.getUser().getEmail())
                .name(userProfile.getName())
                .surname(userProfile.getSurname())
                .address(addressToDto(userProfile.getAddress()))
                .build();
    }

    public static ArenaInfoDto arenaInfoToDto(ArenaEntity arena) {
        return ArenaInfoDto.builder()
                .id(arena.getId())
                .phone(arena.getArenaInfo().getPhone())
                .price(arena.getArenaInfo().getPrice())
                .workedFrom(arena.getArenaInfo().getWorkedFrom())
                .workedTo(arena.getArenaInfo().getWorkedTo())
                .createdAt(arena.getArenaInfo().getCreatedAt())
                .updatedAt(arena.getArenaInfo().getUpdatedAt())
                .address(addressToDto(arena.getArenaInfo().getAddress()))
                .build();
    }

    public static ArenaDto arenaToDto(ArenaEntity arena) {
        return ArenaDto.builder()
                .id(arena.getId())
                .name(arena.getName())
                .description(arena.getDescription())
                .status(arena.isStatus())
                .arenaInfo(arenaInfoToDto(arena))
                .image(arena.getImage())
                .build();
    }

    public static ReservationArenaDto reservationArenaToDto(ReservationArena reservationArena) {
        return ReservationArenaDto.builder()
                .arenaId(reservationArena.getArena().getId())
                .bookingFrom(reservationArena.getBookingFrom())
                .bookingTo(reservationArena.getBookingTo())
                .description(reservationArena.getDescription())
                .totalPrice(reservationArena.getTotalPrice())
                .costumer(reservationArena.getCostumer())
                .build();
    }

    public static PrivilegeDto privilegeToDto(Privilege privilege) {
        return PrivilegeDto.builder()
                .name(privilege.getName())
                .build();
    }

    public static RolePrivilegeDto rolePrivilegeToDto(RolePrivilege privilege) {
        return RolePrivilegeDto.builder()
                .privilege(privilege.getPrivilege().getName())
                .build();
    }

    public static RoleDto roleToDto(RoleEntity role) {
        return RoleDto.builder()
                .name(role.getName())
                .rolePrivileges(role.getRolePrivileges().stream()
                        .map(ConvertEntityToDto::rolePrivilegeToDto)
                        .collect(Collectors.toList()))
                .build();
    }
}

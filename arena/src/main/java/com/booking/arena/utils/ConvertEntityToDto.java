package com.booking.arena.utils;

import com.booking.arena.dto.address.AddressDto;
import com.booking.arena.dto.address.LocationDto;
import com.booking.arena.dto.auth.SignUpDto;
import com.booking.arena.dto.user.RoleDto;
import com.booking.arena.dto.user.UserDto;
import com.booking.arena.dto.user.UserProfileDto;
import com.booking.arena.entity.address.Address;
import com.booking.arena.entity.address.CityEntity;
import com.booking.arena.entity.address.CountryEntity;
import com.booking.arena.entity.user.UserEntity;
import com.booking.arena.entity.user.UserProfile;

public class ConvertEntityToDto {
    public static LocationDto cityToDto(CityEntity city) {
        return LocationDto.builder()
                .name(city.getName())
                .build();
    }

    public static LocationDto countryToDto(CountryEntity country) {
        return LocationDto.builder()
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
                .username(user.getUsername())
                .email(user.getEmail())
                .role(RoleDto.builder()
                        .name(user.getRole().getName())
                        .build())
                .isActive(user.isActive())
                .userProfile(UserProfileDto.builder()
                        .name(user.getUserProfile().getName())
                        .surname(user.getUserProfile().getSurname())
                        .address(addressToDto(user.getUserProfile().getAddress()))
                        .build())
                .build();
    }

    public static UserProfileDto userProfileToDto(UserProfile userProfile) {
        return UserProfileDto.builder()
                .username(userProfile.getUser().getUsername())
                .email(userProfile.getUser().getEmail())
                .name(userProfile.getName())
                .surname(userProfile.getSurname())
                .address(addressToDto(userProfile.getAddress()))
                .build();
    }
}

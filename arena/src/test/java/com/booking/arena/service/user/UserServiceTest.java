package com.booking.arena.service.user;

import com.booking.arena.dto.address.AddressDto;
import com.booking.arena.dto.address.CityDto;
import com.booking.arena.dto.address.CountryDto;
import com.booking.arena.dto.auth.SignUpDto;
import com.booking.arena.dto.user.*;
import com.booking.arena.dto.user.update.UserUpdateDto;
import com.booking.arena.entity.address.Address;
import com.booking.arena.entity.address.CityEntity;
import com.booking.arena.entity.address.CountryEntity;
import com.booking.arena.entity.user.*;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.repository.user.PrivilegeRepository;
import com.booking.arena.repository.user.RoleRepository;
import com.booking.arena.repository.user.UserRepository;
import com.booking.arena.service.address.AddressService;
import com.booking.arena.service.address.AddressServiceImpl;
import com.booking.arena.service.email.EmailServiceImpl;
import com.booking.arena.service.email.EmailVerificationService;
import com.booking.arena.utils.ConvertEntityToDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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
public class UserServiceTest {
    @Mock
    RoleRepository roleRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    private EmailVerificationService emailVerificationService;
    @Mock
    AddressService addressService;
    @Mock
    EmailServiceImpl emailService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;
//    @InjectMocks
//    AddressServiceImpl addressService;

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
    SignUpDto signUpDto;

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
        signUpDto = SignUpDto.builder()
                .email("email")
                .password("password")
                .username("username")
                .userProfile(userProfileDto)
                .build();
    }

    @Test
    void UserService_createUser_returnUserDto() {
        when(roleRepository.findByName(any())).thenReturn(Optional.ofNullable(role));
        when(emailVerificationService.generateCode()).thenReturn("1234");
        when(addressService.create(any())).thenReturn(Optional.of(address));
        UserDto userTest = userService.create(signUpDto).orElseThrow();
        assertAll(
                () -> assertThat(userTest).isNotNull(),
                () -> assertThat(userTest.getUsername()).isEqualTo(user.getUsername()),
                () -> assertThat(userTest.getEmail()).isEqualTo(user.getEmail()),
                () -> assertThat(userTest.getRole().getName()).isEqualTo(role.getName()),
                () -> assertThat(userTest.getUserProfile().getName()).isEqualTo(userProfile.getName()),
                () -> assertThat(userTest.getUserProfile().getSurname()).isEqualTo(userProfile.getSurname()),
                () -> assertThat(userTest.getUserProfile().getAddress().getStreet()).isEqualTo(address.getStreet()),
                () -> assertThat(userTest.getUserProfile().getAddress().getCity().getName()).isEqualTo(address.getCity().getName()),
                () -> assertThat(userTest.getUserProfile().getAddress().getCountry().getName()).isEqualTo(address.getCountry().getName())
        );
    }

    @Test
    void UserService_updateUser_returnUserDto() {
        when(roleRepository.findByName(any())).thenReturn(Optional.ofNullable(updatedRole));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        UserDto userTest = userService.update(1L, updatedUserDto).orElseThrow();
        assertAll(
                () -> assertThat(userTest).isNotNull(),
                () -> assertThat(userTest.getUsername()).isEqualTo(updatedUser.getUsername()),
                () -> assertThat(userTest.getEmail()).isEqualTo(updatedUser.getEmail()),
                () -> assertThat(userTest.getRole().getName()).isEqualTo(updatedUser.getRole().getName()),
                () -> assertThat(userTest.getUserProfile().getName()).isEqualTo(userProfile.getName()),
                () -> assertThat(userTest.getUserProfile().getSurname()).isEqualTo(userProfile.getSurname()),
                () -> assertThat(userTest.getUserProfile().getAddress().getStreet()).isEqualTo(address.getStreet()),
                () -> assertThat(userTest.getUserProfile().getAddress().getCity().getName()).isEqualTo(address.getCity().getName()),
                () -> assertThat(userTest.getUserProfile().getAddress().getCountry().getName()).isEqualTo(address.getCountry().getName())
        );
    }

    @Test
    void UserService_getByIdUser_returnUserDto() {
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        UserDto userTest = userService.getById(1L).orElseThrow();
        assertAll(
                () -> assertThat(userTest).isNotNull(),
                () -> assertThat(userTest.getUsername()).isEqualTo(user.getUsername()),
                () -> assertThat(userTest.getEmail()).isEqualTo(user.getEmail()),
                () -> assertThat(userTest.getRole().getName()).isEqualTo(role.getName()),
                () -> assertThat(userTest.getUserProfile().getName()).isEqualTo(userProfile.getName()),
                () -> assertThat(userTest.getUserProfile().getSurname()).isEqualTo(userProfile.getSurname()),
                () -> assertThat(userTest.getUserProfile().getAddress().getStreet()).isEqualTo(address.getStreet()),
                () -> assertThat(userTest.getUserProfile().getAddress().getCity().getName()).isEqualTo(address.getCity().getName()),
                () -> assertThat(userTest.getUserProfile().getAddress().getCountry().getName()).isEqualTo(address.getCountry().getName())
        );
    }


    @Test
    void UserService_getAll_returnListUserDto() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<UserDto> userTest = userService.getAll();
        verify(userRepository).findAll();
        assertAll(
                () -> assertThat(userTest).isNotNull()
        );
    }

    @Test
    void UserService_delete_returnNon() {
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        assertAll(
                () -> userService.delete(1L)
        );
    }

    @Test
    void UserService_checkAllMethodsOnThrows_returnThrows() {
        assertAll(
                () -> assertThrows(ResourceNotFoundException.class, () -> userService.update(1L, updatedUserDto)),
                () -> assertThrows(ResourceNotFoundException.class, () -> userService.getById(1L)),
                () -> assertThrows(ResourceNotFoundException.class, () -> userService.delete(1L))
        );
    }
}

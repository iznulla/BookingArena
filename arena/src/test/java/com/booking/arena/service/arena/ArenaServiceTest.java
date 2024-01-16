package com.booking.arena.service.arena;

import com.booking.arena.dto.address.AddressDto;
import com.booking.arena.dto.address.CityDto;
import com.booking.arena.dto.address.CountryDto;
import com.booking.arena.dto.arena.ArenaDto;
import com.booking.arena.dto.user.*;
import com.booking.arena.dto.user.update.UserUpdateDto;
import com.booking.arena.entity.address.Address;
import com.booking.arena.entity.address.CityEntity;
import com.booking.arena.entity.address.CountryEntity;
import com.booking.arena.entity.arena.ArenaEntity;
import com.booking.arena.entity.arena.ArenaInfo;
import com.booking.arena.entity.user.*;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.repository.arena.ArenaRepository;
import com.booking.arena.repository.user.UserRepository;
import com.booking.arena.security.UserPrincipal;
import com.booking.arena.service.filesistem.ImageService;
import com.booking.arena.service.user.UserService;
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
public class ArenaServiceTest {
    @Mock
    ArenaRepository arenaRepository;
    @Mock
    UserService userService;
    @Mock
    ImageService imageService;
    @Mock
    ArenaInfoService arenaInfoService;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    ArenaServiceImpl arenaService;

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
    Mock Multipart file
     */
    MockMultipartFile multipartFile;


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
        multipartFile = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "File Content".getBytes());
    }

    @Test
    void ArenaService_createArena_returnArenaDto() {
        try (MockedStatic<SecurityUtils> utilities = Mockito.mockStatic(SecurityUtils.class)) {
            when(userRepository.findById(userPrincipal.getUserId())).thenReturn(Optional.of(user));
            utilities.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(arenaInfoService.create(any())).thenReturn(Optional.of(arenaInfo));
            String arenaInputData = """
                    {
                          "name": "Budapest Arena",
                          "description": "World's best arena",
                          "status": true,
                          "arenaInfo": {
                            "phone": "+998339191919",
                            "price": 125000,
                            "workedFrom": "2023-12-28T05:49:03.469Z",
                            "workedTo": "2023-12-28T05:49:03.469Z",
                            "createdAt": "2023-12-28T05:49:03.469Z",
                            "updatedAt": "2023-12-28T05:49:03.469Z",
                            "address": {
                              "street": "Buyuk Ipak Yuli",
                              "country": {
                                "name": "Uzbekistan"
                              },
                              "city": {
                                "name": "Tashkent"
                              },
                              "longitude": 41.31559857575971,
                              "latitude": 69.29727852274152
                            }
                          }
                        }""";

            ArenaDto arenaTest = arenaService.create(arenaInputData, multipartFile).orElseThrow();
            assertAll(
                    () -> assertThat(arenaTest).isNotNull(),
                    () -> assertThat(arenaTest.getName()).isEqualTo(arena.getName()),
                    () -> assertThat(arenaTest.getDescription()).isEqualTo(arena.getDescription()),
                    () -> assertThat(arenaTest.getImage()).isEqualTo(arena.getImage()),
                    () -> assertThat(arenaTest.getArenaInfo().getPhone()).isEqualTo(arena.getArenaInfo().getPhone()),
                    () -> assertThat(arenaTest.getArenaInfo().getPrice()).isEqualTo(arena.getArenaInfo().getPrice()),
                    () -> assertThat(arenaTest.getArenaInfo().getAddress().getStreet()).isEqualTo(arena.getArenaInfo().getAddress().getStreet())
            );
        }
    }

    @Test
    void ArenaService_updateArena_returnArenaDto() {
        when(arenaRepository.findById(1L)).thenReturn(Optional.of(arena));
        when(arenaInfoService.update(any(), any())).thenReturn(Optional.of(updatedArenaInfo));
        String arenaInputData = """
                {
                      "name": "Allianz Arena",
                      "description": "World's true arena",
                      "status": true,
                      "arenaInfo": {
                        "phone": "+998336161616",
                        "price": 120000,
                        "workedFrom": "2023-12-28T05:49:03.469Z",
                        "workedTo": "2023-12-28T05:49:03.469Z",
                        "createdAt": "2023-12-28T05:49:03.469Z",
                        "updatedAt": "2023-12-28T05:49:03.469Z",
                        "address": {
                          "street": "Maksim Gorkiy",
                          "country": {
                            "name": "Uzbekistan"
                          },
                          "city": {
                            "name": "Tashkent"
                          },
                          "longitude": 41.31559857575971,
                          "latitude": 69.29727852274152
                        }
                      }
                    }""";
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "File Content".getBytes());
        ArenaDto arenaTest = arenaService.update(1L, arenaInputData, multipartFile).orElseThrow();
        assertAll(
                () -> assertThat(arenaTest).isNotNull(),
                () -> assertThat(arenaTest.getName()).isEqualTo(updateArena.getName()),
                () -> assertThat(arenaTest.getDescription()).isEqualTo(arena.getDescription()),
                () -> assertThat(arenaTest.getImage()).isEqualTo(arena.getImage()),
                () -> assertThat(arenaTest.getArenaInfo().getPhone()).isEqualTo(arena.getArenaInfo().getPhone()),
                () -> assertThat(arenaTest.getArenaInfo().getPrice()).isEqualTo(arena.getArenaInfo().getPrice()),
                () -> assertThat(arenaTest.getArenaInfo().getAddress().getStreet()).isEqualTo(arena.getArenaInfo().getAddress().getStreet())
        );
    }

    @Test
    void ArenaService_getByIdArena_returnArenaDto() {
        when(arenaRepository.findById(1L)).thenReturn(Optional.of(arena));
        ArenaDto arenaTest = arenaService.getById(1L).orElseThrow();
        assertAll(
                () -> assertThat(arenaTest).isNotNull(),
                () -> Assertions.assertEquals(arenaTest, ConvertEntityToDto.arenaToDto(arena)),
                () -> Assertions.assertEquals(arenaTest.getArenaInfo(), ConvertEntityToDto.arenaInfoToDto(arena))
        );
    }


    @Test
    void ArenaService_getAll_returnListArenaDto() {
        when(arenaRepository.findAll()).thenReturn(List.of(arena));
        List<ArenaDto> arenaTest = arenaService.getAll();
        verify(arenaRepository).findAll();
        assertAll(
                () -> assertThat(arenaTest).isNotNull()
        );
    }

    @Test
    void ArenaService_delete_returnNon() {
        try (MockedStatic<SecurityUtils> utilities = Mockito.mockStatic(SecurityUtils.class)) {
            utilities.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(arenaRepository.findById(any())).thenReturn(Optional.of(arena));
            assertAll(
                    () -> arenaService.delete(1L)
            );
        }
    }

    @Test
    void ArenaService_checkAllMethodsOnThrows_returnThrows() {
        assertAll(
                () -> assertThrows(ResourceNotFoundException.class, () -> arenaService.update(1L, any(), multipartFile)),
                () -> assertThrows(ResourceNotFoundException.class, () -> arenaService.getById(1L)),
                () -> assertThrows(ResourceNotFoundException.class, () -> arenaService.delete(1L))
        );
    }
}

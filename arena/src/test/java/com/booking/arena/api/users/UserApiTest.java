package com.booking.arena.api.users;

import com.booking.arena.dto.address.AddressDto;
import com.booking.arena.dto.address.CityDto;
import com.booking.arena.dto.address.CountryDto;
import com.booking.arena.dto.auth.LoginResponseDto;
import com.booking.arena.dto.auth.SignUpDto;
import com.booking.arena.dto.user.*;
import com.booking.arena.dto.user.update.UserUpdateDto;
import com.booking.arena.utils.CustomRequestUrl;
import io.restassured.RestAssured;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static io.restassured.RestAssured.given;

@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserApiTest {
    @LocalServerPort
    private Integer port;
    LoginResponseDto token;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    SignUpDto userDto = SignUpDto.builder()
            .username("famin")
            .password("123")
            .email("fami@imale.com")
            .userProfile(UserProfileDto.builder()
                    .name("famin")
                    .surname("famin")
                    .address(AddressDto.builder()
                            .street("Mirobod")
                            .latitude(123.123)
                            .longitude(123.123)
                            .city(CityDto.builder()
                                    .name("Samarkand")
                                    .build())
                            .country(CountryDto.builder()
                                    .name("Uzbekistan")
                                    .build())
                            .build())
                    .build())
            .build();
    UserUpdateDto userUpdateDto = UserUpdateDto.builder()
            .username("famina")
            .email("fami@imale.ru")
            .role("ADMIN")
            .isActive(true)
            .build();


    @BeforeEach
    void setUp() {
        token = CustomRequestUrl
                .getToken("admin", "admin", "http://localhost:" + port + "/login", LoginResponseDto.class);

        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    @Order(1)
    void UserApiTest_getAllUser_returnedListUserDto() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .contentType("application/json")
                .when()
                .get("/api/v1/users")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(2)
    void UserApiTest_postCreateUser_returnedUserDto() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .body(userDto)
                .contentType("application/json")
                .when()
                .post("/api/v1/users")
                .then()
                .statusCode(201);
    }


    @Test
    @Order(3)
    void UserApiTest_getByIdUser_returnedUserDto() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .contentType("application/json")
                .when()
                .get("/api/v1/users/1")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(4)
    void UserApiTest_patchUpdateUser_returnedUserDto() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .body(userUpdateDto)
                .contentType("application/json")
                .when()
                .patch("/api/v1/users/1")
                .then()
                .statusCode(201);
    }

    @Test
    @Order(5)
    void UserApiTest_deleteUser_returnedNoContent() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .when()
                .delete("/api/v1/users/1")
                .then()
                .statusCode(204);
    }
}

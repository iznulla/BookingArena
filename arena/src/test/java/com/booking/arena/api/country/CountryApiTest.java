package com.booking.arena.api.country;

import com.booking.arena.dto.address.CountryDto;
import com.booking.arena.dto.auth.LoginResponseDto;
import com.booking.arena.repository.address.CountryRepository;
import com.booking.arena.utils.CustomRequestUrl;
import io.restassured.RestAssured;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

import static io.restassured.RestAssured.given;

@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CountryApiTest {
    @LocalServerPort
    private Integer port;

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
    LoginResponseDto token;

    CountryDto countryDto = CountryDto.builder()
            .name("Africa")
            .build();
    CountryDto updateCountryDto = CountryDto.builder()
            .name("Russia")
            .build();
    @Autowired
    CountryRepository countryRepository;

    Long id;

    @BeforeEach
    void setUp() {
        token = CustomRequestUrl
                .getToken("admin", "admin", "http://localhost:" + port + "/login", LoginResponseDto.class);

        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    @Order(1)
    void CountryApiTest_getAllCountry_returnedListCountryDto() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .contentType("application/json")
                .when()
                .get("/api/v1/country")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(2)
    void CountryApiTest_postCreateCountry_returnedCountryDto() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .body(countryDto)
                .contentType("application/json")
                .when()
                .post("/api/v1/country")
                .then()
                .statusCode(201);
        id = countryRepository.findByName("Uzbekistan").orElseThrow().getId();
    }


    @Test
    @Order(3)
    void CountryApiTest_getByIdCountry_returnedCountryDto() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .contentType("application/json")
                .when()
                .get("/api/v1/country/1")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(4)
    void CountryApiTest_patchUpdateCountry_returnedCountryDto() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .body(updateCountryDto)
                .contentType("application/json")
                .when()
                .patch("/api/v1/country/1")
                .then()
                .statusCode(201);
    }

    @Test
    @Order(5)
    void CountryApiTest_deleteCountry_returnedNoContent() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .when()
                .delete("/api/v1/country/1")
                .then()
                .statusCode(204);
//        Assertions.assertEquals(1L, cityRepository.findByName("Samarkand").orElseThrow().getCountry().getId());
    }
}

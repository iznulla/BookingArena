package com.booking.arena.api.city;

import com.booking.arena.dto.address.CityDto;
import com.booking.arena.dto.auth.LoginResponseDto;
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

import static io.restassured.RestAssured.given;

@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CityApiTest {
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

    CityDto cityDto = CityDto.builder()
            .name("Dubai")
            .countryName("Uzbekistan")
            .build();
    CityDto updateCityDto = CityDto.builder()
            .name("Istanbul")
            .countryName("Uzbekistan")
            .build();


    @BeforeEach
    void setUp() {
        token = CustomRequestUrl
                .getToken("admin", "admin", "http://localhost:" + port + "/login", LoginResponseDto.class);

        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    @Order(1)
    void CityApiTest_getAllCity_returnedListCityDto() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .contentType("application/json")
                .when()
                .get("/api/v1/city")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(2)
    void CityApiTest_postCreateCity_returnedCityDto() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .body(cityDto)
                .contentType("application/json")
                .when()
                .post("/api/v1/city")
                .then()
                .statusCode(201);
    }


    @Test
    @Order(3)
    void CityApiTest_getByIdCity_returnedCityDto() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .contentType("application/json")
                .when()
                .get("/api/v1/city/1")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(4)
    void CityApiTest_patchUpdateCity_returnedCityDto() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .body(updateCityDto)
                .contentType("application/json")
                .when()
                .patch("/api/v1/city/1")
                .then()
                .statusCode(201);
    }

    @Test
    @Order(5)
    void CityApiTest_deleteCity_returnedNoContent() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .when()
                .delete("/api/v1/city/1")
                .then()
                .statusCode(204);
    }
}


package com.booking.arena.api.booking;

import com.booking.arena.dto.auth.LoginResponseDto;
import com.booking.arena.dto.booking.ReservationArenaDto;
import com.booking.arena.utils.CustomRequestUrl;
import io.restassured.RestAssured;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static io.restassured.RestAssured.given;

@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReservationApiTest {
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

    ReservationArenaDto reservationArenaDto = ReservationArenaDto.builder()
            .arenaId(1L)
            .bookingFrom(Instant.now())
            .bookingTo(Instant.now().plus(60, ChronoUnit.MINUTES))
            .costumer("admin")
            .description("test")
            .build();

    @BeforeEach
    void setUp() {
        token = CustomRequestUrl
                .getToken("admin", "admin", "http://localhost:" + port + "/login", LoginResponseDto.class);

        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    @Order(1)
    void ReservationArenaApiTest_getAllReservationArena_returnedListReservationArenaDto() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .contentType("application/json")
                .when()
                .get("/api/v1/reservation")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(2)
    void ReservationArenaApiTest_postCreateReservationArena_returnedReservationArenaDto() {
        given()
                .header("Authorization", "Bearer " + token.getAccessToken())
                .body(reservationArenaDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/v1/reservation")
                .then()
                .statusCode(201);
    }

    @Test
    @Order(3)
    void ReservationArenaApiTest_getByIdReservationArena_returnedReservationArenaDto() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .contentType("application/json")
                .when()
                .get("/api/v1/reservation/1")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(4)
    void ReservationArenaApiTest_patchUpdateReservationArena_returnedReservationArenaDto() {
        reservationArenaDto.setBookingFrom(Instant.now().plus(60, ChronoUnit.MINUTES));
        reservationArenaDto.setBookingTo(Instant.now().plus(120, ChronoUnit.MINUTES));
        given()
                .header("Authorization", "Bearer " + token.getAccessToken())
                .body(reservationArenaDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("/api/v1/reservation/1")
                .then()
                .statusCode(201);
    }

    @Test
    @Order(5)
    void ReservationArenaApiTest_deleteReservationArena_returnedNoContent() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .when()
                .delete("/api/v1/reservation/1")
                .then()
                .statusCode(204);
    }
}

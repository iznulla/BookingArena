package com.booking.arena.api.privilege;

import com.booking.arena.dto.auth.LoginResponseDto;
import com.booking.arena.dto.user.PrivilegeDto;
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
public class PrivilegeApiTest  {
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

    PrivilegeDto privilegeDto = PrivilegeDto.builder()
            .name("Test")
            .build();
    PrivilegeDto updatePrivilegeDto = PrivilegeDto.builder()
            .name("Update")
            .build();


    @BeforeEach
    void setUp() {
        token = CustomRequestUrl
                .getToken("admin", "admin", "http://localhost:" + port + "/login", LoginResponseDto.class);

        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    @Order(1)
    void PrivilegeApiTest_getAllPrivilege_returnedListPrivilegeDto() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .contentType("application/json")
                .when()
                .get("/api/v1/privilege")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(2)
    void PrivilegeApiTest_postCreatePrivilege_returnedPrivilegeDto() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .body(privilegeDto)
                .contentType("application/json")
                .when()
                .post("/api/v1/privilege")
                .then()
                .statusCode(201);
    }


    @Test
    @Order(3)
    void PrivilegeApiTest_getByIdPrivilege_returnedPrivilegeDto() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .contentType("application/json")
                .when()
                .get("/api/v1/privilege/1")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(4)
    void PrivilegeApiTest_patchUpdatePrivilege_returnedPrivilegeDto() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .body(updatePrivilegeDto)
                .contentType("application/json")
                .when()
                .patch("/api/v1/privilege/1")
                .then()
                .statusCode(201);
    }

    @Test
    @Order(5)
    void PrivilegeApiTest_deletePrivilege_returnedNoContent() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .when()
                .delete("/api/v1/privilege/1")
                .then()
                .statusCode(204);
    }
}

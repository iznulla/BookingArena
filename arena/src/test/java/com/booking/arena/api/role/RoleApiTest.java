package com.booking.arena.api.role;

import com.booking.arena.dto.auth.LoginResponseDto;
import com.booking.arena.dto.user.PrivilegeDto;
import com.booking.arena.dto.user.RoleDto;
import com.booking.arena.dto.user.RolePrivilegeDto;
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
public class RoleApiTest {
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

    RoleDto roleDto = RoleDto.builder()
            .name("Test")
            .rolePrivileges(List.of(RolePrivilegeDto.builder()
                    .privilege("CREATE")
                    .build()))
            .build();
    RoleDto updateRoleDto = RoleDto.builder()
            .name("Update")
            .rolePrivileges(List.of(RolePrivilegeDto.builder()
                    .privilege("UPDATE")
                    .build()))
            .build();

    PrivilegeDto privilegeDto = PrivilegeDto.builder()
            .name("UPDATE")
            .build();

    @BeforeEach
    void setUp() {
        token = CustomRequestUrl
                .getToken("admin", "admin", "http://localhost:" + port + "/login", LoginResponseDto.class);

        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    @Order(1)
    void RoleApiTest_getAllRole_returnedListRoleDto() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .contentType("application/json")
                .when()
                .get("/api/v1/role")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(2)
    void RoleApiTest_postCreateRole_returnedRoleDto() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .body(roleDto)
                .contentType("application/json")
                .when()
                .post("/api/v1/role")
                .then()
                .statusCode(201);
    }


    @Test
    @Order(3)
    void RoleApiTest_getByIdRole_returnedRoleDto() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .contentType("application/json")
                .when()
                .get("/api/v1/role/1")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(4)
    void RoleApiTest_patchUpdateRole_returnedRoleDto() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .body(updateRoleDto)
                .contentType("application/json")
                .when()
                .patch("/api/v1/role/1")
                .then()
                .statusCode(201);
    }

    @Test
    @Order(5)
    void RoleApiTest_deleteRole_returnedNoContent() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .when()
                .delete("/api/v1/role/2")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(6)
    void RoleApiTest_deletePrivilege_returnedNoContent() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .body(privilegeDto)
                .contentType("application/json")
                .when()
                .delete("/api/v1/role/1/privileges")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(4)
    void RoleApiTest_patchUpdateRoleAddPrivileges_returnedRoleDto() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .body(List.of(privilegeDto))
                .contentType("application/json")
                .when()
                .patch("/api/v1/role/1/privileges")
                .then()
                .statusCode(201);
    }
}

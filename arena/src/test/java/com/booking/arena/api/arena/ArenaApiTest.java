package com.booking.arena.api.arena;

import com.booking.arena.dto.auth.LoginResponseDto;
import com.booking.arena.utils.CustomRequestUrl;
import io.restassured.RestAssured;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;

import static io.restassured.RestAssured.given;

@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ArenaApiTest {
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
    String updateArenaInputData = """
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


    @BeforeEach
    void setUp() {
        token = CustomRequestUrl
                .getToken("admin", "admin", "http://localhost:" + port + "/login", LoginResponseDto.class);

        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    @Order(1)
    void ArenaApiTest_getAllArena_returnedListArenaDto() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .contentType("application/json")
                .when()
                .get("/api/v1/arena")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(2)
    void ArenaApiTest_postCreateArena_returnedArenaDto() throws IOException {
        given()
                .multiPart("file", "test.txt", multipartFile.getBytes(), MediaType.TEXT_PLAIN_VALUE)
                .multiPart("arenaDto", arenaInputData)
                .header("Authorization", "Bearer " + token.getAccessToken())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .when()
                .post("/api/v1/arena")
                .then()
                .statusCode(201);
    }


    @Test
    @Order(3)
    void ArenaApiTest_getByIdArena_returnedArenaDto() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .contentType("application/json")
                .when()
                .get("/api/v1/arena/1")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(4)
    void ArenaApiTest_patchUpdateArena_returnedArenaDto() throws IOException {

        given()
                .multiPart("file", "test.txt", multipartFile.getBytes(), MediaType.TEXT_PLAIN_VALUE)
                .multiPart("arenaDto", updateArenaInputData)
                .header("Authorization", "Bearer " + token.getAccessToken())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .when()
                .patch("/api/v1/arena/3")
                .then()
                .statusCode(201);
    }

    @Test
    @Order(5)
    void ArenaApiTest_deleteArena_returnedNoContent() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token.getAccessToken())
                .when()
                .delete("/api/v1/arena/1")
                .then()
                .statusCode(204);
    }
}

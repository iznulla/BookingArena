package com.booking.arena.api.arena;


import com.booking.arena.dto.arena.ArenaDto;
import com.booking.arena.dto.arena.ArenaFiltersDto;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.service.arena.ArenaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/arena")
@Tag(name = "Arena", description = "API для работы с спорт комплексами")
public class ArenaApi {
    private final ArenaService arenaService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ArenaDto.class))}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ResourceNotFoundException.class))}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @Operation(
            summary = "GET all arena",
            description = "Позволяет получить все спорт-комплексов"
    )
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(arenaService.getAll());
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(example = """
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
                    }"""))}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ResourceNotFoundException.class))}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @Operation(
            summary = "POST create arena",
            description = "Позволяет создать комплекс"
    )
    @PreAuthorize("hasAuthority('CREATE_ARENA')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void create(@RequestPart(required = false) MultipartFile file, @RequestPart String arenaDto) throws IOException, InterruptedException {
        arenaService.create(arenaDto, file);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ArenaDto.class))}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ResourceNotFoundException.class))}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @Operation(
            summary = "GET{id} arena by id",
            description = "Позволяет получить спорт комплекс по id"
    )
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(arenaService.getById(id));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(example = """
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
                    }"""))}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ResourceNotFoundException.class))}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @Operation(
            summary = "PATCH{id} update arena by Id and new params in body",
            description = "Позволяет изменить данные спорт-комплекса, предназначен только для админа и менеджера"
    )
    @PreAuthorize("hasAuthority('UPDATE_ARENA')")
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestPart(value = "file", required = false) MultipartFile file,
                                    @RequestPart String arenaDto) {
        return ResponseEntity.ok(arenaService.update(id, arenaDto, file));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ResourceNotFoundException.class))}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @Operation(
            summary = "DELETE{id} arena by Id",
            description = "Позволяет удалить спорт-комплекс по id, только админ"
    )
    @PreAuthorize("hasAuthority('DELETE_ARENA')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        arenaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ArenaDto.class))}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ResourceNotFoundException.class))}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @Operation(
            summary = "GET arena by filters",
            description = "Позволяет получить отфильтрованный список"
    )
    @GetMapping("/filter")
    public ResponseEntity<List<ArenaDto>> getByFilter(ArenaFiltersDto filters) {
        return ResponseEntity.ok(arenaService.getByFilter(filters));
    }
}
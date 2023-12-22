package com.booking.arena.api.arena;

import com.booking.arena.dto.address.CityDto;
import com.booking.arena.dto.arena.ArenaDto;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.service.arena.ArenaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/arena")
@Tag(name = "Arena", description = "API для работы с спорт комплексами")
public class ArenaApi {
    private final ArenaService arenaService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ArenaDto.class)) }),
            @ApiResponse(responseCode = "404",content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "GET all arena",
            description = "Позволяет получить все спорт-комплексов"
    )
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(arenaService.getAll());
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = ArenaDto.class)) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "POST create arena",
            description = "Позволяет создать комплекс"
    )
    @PostMapping
    public ResponseEntity<?> create(@RequestBody ArenaDto arenaDto) {
        return ResponseEntity.ok(arenaService.create(arenaDto));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ArenaDto.class)) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "GET{id} arena by id",
            description = "Позволяет получить спорт комплекс по id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(arenaService.getById(id));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = ArenaDto.class)) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "PATCH{id} update arena by Id and new params in body",
            description = "Позволяет изменить данные спорт-комплекса, предназначен только для админа и менеджера"
    )
    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ArenaDto arenaDto) {
        return ResponseEntity.ok(arenaService.update(id, arenaDto));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "DELETE{id} arena by Id",
            description = "Позволяет удалить спорт-комплекс по id, только админ"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        arenaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

//{
//  "id": 0,
//  "name": "Paxtakor",
//  "description": "Best Football Arena",
//  "status": true,
//  "arenaInfo": {
//    "id": 0,
//    "phone": "+998339191919",
//    "price": 120000,
//    "workedFrom": "2023-12-22T06:49:08.252Z",
//    "workedTo": "2023-12-22T06:49:08.252Z",
//    "createdAt": "2023-12-22T06:49:08.252Z",
//    "updatedAt": "2023-12-22T06:49:08.252Z",
//    "address": {
//      "street": "Chilonzor",
//      "country": {
//        "name": "Uzbekistan"
//      },
//      "city": {
//        "name": "Tashkent",
//        "countryName": "Uzbekistan"
//      },
//      "longitude": 41.45343,
//      "latitude": 64.45664565
//    }
//  }
//}

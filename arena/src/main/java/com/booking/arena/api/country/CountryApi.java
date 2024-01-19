package com.booking.arena.api.country;

import com.booking.arena.dto.address.CountryDto;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.service.address.country.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/country")
@Tag(name = "Countries", description = "API для работы со странами")
@PreAuthorize("hasAuthority('CREATE')")
public class CountryApi {
    private final CountryService countryService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = CountryDto.class))}),
            @ApiResponse(responseCode = "404",content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "GET all countries",
            description = "Позволяет получить все страны"
    )
    @GetMapping
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(countryService.getAll(), HttpStatus.OK);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = CountryDto.class)) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "GET{id} country by id",
            description = "Позволяет получить страну по id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(countryService.getById(id));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = CountryDto.class)) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "POST create country",
            description = "Позволяет создать страну"
    )
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CountryDto countryDto) {
        return new ResponseEntity<>(countryService.create(countryDto), HttpStatus.CREATED);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = CountryDto.class)) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "PATCH{id} update country by Id and new params in body",
            description = "Позволяет изменить данные страны, предназначен только для админа"
    )
    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CountryDto countryDto) {
        return new ResponseEntity<>(countryService.update(id, countryDto), HttpStatus.CREATED);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "DELETE{id} country by Id",
            description = "Позволяет удалить страну, только админ"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        countryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

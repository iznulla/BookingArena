package com.booking.arena.api.booking;

import com.booking.arena.dto.booking.ReservationArenaDto;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.service.booking.ReservationService;
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
@RequestMapping("api/v1/reservation")
@Tag(name = "Reservation", description = "API для работы с бронированием комплексов")
public class ReservationApi {
    private final ReservationService reservationService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ReservationArenaDto.class)) }),
            @ApiResponse(responseCode = "404",content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "GET all reservations",
            description = "Позволяет получить все бронирования спорт-комплексов"
    )
    @PreAuthorize("hasAuthority('UPDATE_RESERVATION')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(reservationService.getAll());
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = ReservationArenaDto.class)) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "POST create reservation",
            description = "Позволяет создать резервацию"
    )
    @PostMapping
    public ResponseEntity<?> create(@RequestBody ReservationArenaDto reservationArenaDto) {
        return new ResponseEntity<>(reservationService.create(reservationArenaDto), HttpStatus.CREATED);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ReservationArenaDto.class)) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "GET{id} reservation by id",
            description = "Позволяет получить бронь комплекс по id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getById(id));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = ReservationArenaDto.class)) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "PATCH{id} update arena by Id and new params in body",
            description = "Позволяет изменить данные бронирования спорт-комплекса, предназначен только для админа и менеджера"
    )
    @PreAuthorize("hasAuthority('UPDATE_RESERVATION')")
    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ReservationArenaDto reservationArenaDto) {
        return new ResponseEntity<>(reservationService.update(id, reservationArenaDto), HttpStatus.CREATED);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "DELETE{id} reservation by Id",
            description = "Позволяет удалить бронь спорт-комплекса по id, только админ"
    )
    @PreAuthorize("hasAuthority('UPDATE_RESERVATION')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

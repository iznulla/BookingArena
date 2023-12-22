package com.booking.arena.api.profile;

import com.booking.arena.dto.user.UserProfileDto;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.service.user.profile.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
@Tag(name = "Profile", description = "API для работы с профилем пользователя")
public class ProfileApi {
    private final UserProfileService userProfileService;

    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = UserProfileDto.class)) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "PATCH{id} update user by Id and new params in body",
            description = "Позволяет изменить данные пользователя, предназначен только для аутенцифицированного пользователя"
    )
    @PatchMapping
    public ResponseEntity<?> update(@RequestParam Long id, @RequestBody UserProfileDto userProfileDto) {
        return new ResponseEntity<>(userProfileService.update(userProfileDto), HttpStatus.valueOf(201));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = UserProfileDto.class)) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "GET{id} profile by user id",
            description = "Позволяет получить пользователей по id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@RequestParam Long id) {
        return new ResponseEntity<>(userProfileService.getById(id), HttpStatus.valueOf(200));
    }
}

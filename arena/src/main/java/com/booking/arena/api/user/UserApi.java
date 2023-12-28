package com.booking.arena.api.user;

import com.booking.arena.dto.user.VerifyDto;
import com.booking.arena.dto.auth.SignUpDto;
import com.booking.arena.dto.user.update.UserUpdateDto;
import com.booking.arena.dto.user.UserDto;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.service.user.UserService;
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
@Tag(name = "Users", description = "Users management APIs")
@RequestMapping("/api/v1/users")
public class UserApi {
    private final UserService userService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = UserDto.class)) }),
            @ApiResponse(responseCode = "404",content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "GET all users",
            description = "Позволяет получить всех пользователей"
    )
    @PreAuthorize("hasAuthority('READ')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.valueOf(200));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = UserDto.class)) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "GET{id} users by id",
            description = "Позволяет получить пользователей по id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
            return new ResponseEntity<>(userService.getById(id).orElseThrow(), HttpStatus.valueOf(200));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = SignUpDto.class)) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "POST create user",
            description = "Позволяет создать пользлвателя"
    )
    @PostMapping
    public ResponseEntity<?> create(@RequestBody SignUpDto signUpDto) {
        return new ResponseEntity<>(userService.create(signUpDto), HttpStatus.valueOf(201));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = UserUpdateDto.class)) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "PATCH{id} update user by Id and new params in body",
            description = "Позволяет изменить данные пользователя, предназначен только для админа"
    )
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody UserUpdateDto userDto) {
        return new ResponseEntity<>(userService.update(id, userDto).orElseThrow(), HttpStatus.valueOf(201));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "DELETE{id} user by Id",
            description = "Позволяет удалить пользователя"
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<>(String.format("Delete user by Id: %d success", id),HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/verify")
    public ResponseEntity<?> verifyUser(@PathVariable Long id, @RequestBody VerifyDto verifyDto){
        if (userService.verification(id, verifyDto.getCode())) {
            return ResponseEntity.ok().build();
        }
        return new ResponseEntity<>("Verification failed", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{id}/resend")
    public ResponseEntity<?> resend(@PathVariable Long id){
        userService.resendVerificationCode(id);
        return ResponseEntity.ok().build();
    }
}

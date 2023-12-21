package com.booking.arena.controller.user;

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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Users", description = "Users management APIs")
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = List.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404",content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "GET all users",
            description = "Позволяет получить всех пользователей"
    )
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.valueOf(200));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = UserDto.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "GET{id} users by id",
            description = "Позволяет получить пользователей по id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
            return new ResponseEntity<>(userService.getUserById(id).orElseThrow(), HttpStatus.valueOf(200));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = SignUpDto.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "POST create user",
            description = "Позволяет создать пользлвателя"
    )
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody SignUpDto signUpDto) {
        return new ResponseEntity<>(userService.createUser(signUpDto), HttpStatus.valueOf(201));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = UserDto.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "PATCH update user by Id and new params in body",
            description = "Позволяет изменить данные пользователя, предназначен только для админа"
    )
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto userDto) {
        return new ResponseEntity<>(userService.updateUser(id, userDto).orElseThrow(), HttpStatus.valueOf(201));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "DELETE user by Id",
            description = "Позволяет удалить пользователя"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(String.format("Delete user by Id: %d success", id),HttpStatus.NO_CONTENT);
    }
}

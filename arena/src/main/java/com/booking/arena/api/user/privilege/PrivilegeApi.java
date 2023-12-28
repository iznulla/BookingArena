package com.booking.arena.api.user.privilege;

import com.booking.arena.dto.user.PrivilegeDto;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.service.user.role.PrivilegeService;
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
@Tag(name = "Privilege", description = "Privilege management APIs")
@RequestMapping("/api/v1/privilege")
public class PrivilegeApi {
    private final PrivilegeService privilegeService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = PrivilegeDto.class)) }),
            @ApiResponse(responseCode = "404",content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "GET all privileges",
            description = "Позволяет получить все разрешения"
    )
    @PreAuthorize("hasAuthority('READ')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(privilegeService.getAll(), HttpStatus.valueOf(200));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = PrivilegeDto.class)) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "GET{id} privilege by id",
            description = "Позволяет получить разрешения по id"
    )
    @PreAuthorize("hasAuthority('READ')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return new ResponseEntity<>(privilegeService.getById(id).orElseThrow(), HttpStatus.valueOf(200));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = PrivilegeDto.class)) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "POST create privilege",
            description = "Позволяет создать разрешения"
    )
    @PreAuthorize("hasAuthority('CREATE')")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody PrivilegeDto privilegeDto) {
        return new ResponseEntity<>(privilegeService.create(privilegeDto), HttpStatus.valueOf(201));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = PrivilegeDto.class)) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "PATCH{id} update privilege by Id and new params in body",
            description = "Позволяет изменить данные разрешения, предназначен только для админа"
    )
    @PreAuthorize("hasAuthority('UPDATE')")
    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody PrivilegeDto privilegeDto) {
        return new ResponseEntity<>(privilegeService.update(id, privilegeDto), HttpStatus.valueOf(201));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "DELETE{id} privilege by Id",
            description = "Позволяет удалить разрешения"
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        privilegeService.delete(id);
        return new ResponseEntity<>(String.format("Delete privilege by Id: %d success", id),HttpStatus.NO_CONTENT);
    }
}

package com.booking.arena.api.user.role;

import com.booking.arena.dto.user.PrivilegeDto;
import com.booking.arena.dto.user.RoleDto;
import com.booking.arena.entity.user.Privilege;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.repository.user.PrivilegeRepository;
import com.booking.arena.service.user.role.RoleService;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Role", description = "Role management APIs")
@RequestMapping("/api/v1/role")
public class RoleApi {
    private final RoleService roleService;
    private final PrivilegeRepository privilegeRepository;

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = RoleDto.class)) }),
            @ApiResponse(responseCode = "404",content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "GET all roles",
            description = "Позволяет получить все роли"
    )
    @PreAuthorize("hasAuthority('READ')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(roleService.getAll(), HttpStatus.valueOf(200));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = RoleDto.class)) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "GET{id} role by id",
            description = "Позволяет получить роль по id"
    )
    @PreAuthorize("hasAuthority('READ')")
    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getById(@PathVariable Long id) {
        return new ResponseEntity<>(roleService.getById(id).orElseThrow(), HttpStatus.valueOf(200));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = RoleDto.class)) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "POST create role",
            description = "Позволяет создать роль"
    )
//    @PreAuthorize("hasAuthority('CREATE')")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody RoleDto roleDto) {
        return new ResponseEntity<>(roleService.create(roleDto), HttpStatus.valueOf(201));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = RoleDto.class)) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "PATCH{id} update role by Id and new params in body",
            description = "Позволяет изменить данные роли, предназначен только для админа"
    )
//    @PreAuthorize("hasAuthority('UPDATE')")
    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody RoleDto roleDto) {
        return new ResponseEntity<>(roleService.update(id, roleDto), HttpStatus.valueOf(201));
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
        roleService.delete(id);
        return new ResponseEntity<>(String.format("Delete role by Id: %d success", id), HttpStatus.NO_CONTENT);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = RoleDto.class)) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "DELETE{id} privilege by Id from role",
            description = "Позволяет удалить разрешения из роли"
    )
//    @PreAuthorize("hasAuthority('DELETE')")
    @DeleteMapping("/{id}/privileges")
    public ResponseEntity<?> deletePrivilege(@PathVariable Long id, @RequestBody PrivilegeDto privilegeDto) {
        Privilege privilege = privilegeRepository.findByName(privilegeDto.getName()).orElseThrow(() ->
                new ResourceNotFoundException("Privilege not found by name: "+ privilegeDto.getName()));
        roleService.deletePrivilege(id, privilege.getId());
        return new ResponseEntity<>(String.format("Delete privilege from role by Id: %d success", id), HttpStatus.NO_CONTENT);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = RoleDto.class)) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ResourceNotFoundException.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Operation(
            summary = "PATCH{id} update role by Id add new privileges",
            description = "Позволяет добавить привилегии ролям, предназначен только для админа"
    )
//    @PreAuthorize("hasAuthority('UPDATE')")
    @PatchMapping("/{id}/privileges")
    public ResponseEntity<?> addPrivilege(@PathVariable Long id, @RequestBody List<PrivilegeDto> privilegeDto) {
        return new ResponseEntity<>(roleService.addPrivilege(id, privilegeDto), HttpStatus.valueOf(201));
    }

}

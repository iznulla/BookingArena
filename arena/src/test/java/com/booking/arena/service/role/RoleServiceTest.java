package com.booking.arena.service.role;

import com.booking.arena.dto.user.PrivilegeDto;
import com.booking.arena.dto.user.RoleDto;
import com.booking.arena.dto.user.RolePrivilegeDto;
import com.booking.arena.entity.user.Privilege;
import com.booking.arena.entity.user.RoleEntity;
import com.booking.arena.entity.user.RolePrivilege;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.repository.user.PrivilegeRepository;
import com.booking.arena.repository.user.RoleRepository;
import com.booking.arena.service.user.role.RoleServiceImpl;
import com.booking.arena.utils.ConvertEntityToDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    @Mock
    PrivilegeRepository privilegeRepository;
    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    RoleServiceImpl roleService;

    /*
    Role data
     */
    RoleEntity role;
    RoleEntity updatedRole;

    RoleDto roleDto;
    RoleDto updatedRoleDto;

    /*
    Privilege data
     */
    PrivilegeDto privilegeDto;
    PrivilegeDto updatedPrivilegeDto;
    Privilege privilege;
    Privilege updatedPrivilege;

    /*
    RolePrivilege data
     */

    RolePrivilege rolePrivilege;
    RolePrivilegeDto rolePrivilegeDto;
    RolePrivilege updatedRolePrivilege;

    @BeforeEach
    void setUp() {
        privilegeDto = PrivilegeDto.builder()
                .name("READ")
                .build();
        updatedPrivilegeDto = PrivilegeDto.builder()
                .name("Update")
                .build();
        privilege = Privilege.builder()
                .id(1L)
                .name("READ")
                .build();
        updatedPrivilege = Privilege.builder()
                .id(2L)
                .name("Update")
                .build();

        role = RoleEntity.builder()
                .id(1L)
                .name("ADMIN")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        updatedRole = RoleEntity.builder()
                .id(1L)
                .name("UPDATE")
                .updatedAt(Instant.now())
                .build();
        updatedRolePrivilege = RolePrivilege.builder()
                .privilege(updatedPrivilege)
                .build();
        rolePrivilege = RolePrivilege.builder()
                .id(1L)
                .privilege(privilege)
                .role(role)
                .build();
        rolePrivilegeDto = RolePrivilegeDto.builder()
                .privilege("ADMIN")
                .build();
        roleDto = RoleDto.builder()
                .name("ADMIN")
                .rolePrivileges(List.of(rolePrivilegeDto))
                .build();
        updatedRoleDto = RoleDto.builder()
                .name("UPDATE")
                .build();

        role.setRolePrivileges(List.of(rolePrivilege));

    }

    @Test
    void RoleService_createRole_returnRoleDto() {
        when(privilegeRepository.findByName(any())).thenReturn(Optional.of(privilege));
        RoleDto roleTest = roleService.create(roleDto).orElseThrow();
        RoleDto compare = ConvertEntityToDto.roleToDto(role);
        assertAll(
                () -> assertThat(roleTest).isNotNull(),
                () -> assertThat(roleTest.getName()).isEqualTo(role.getName()),
                () -> assertThat(roleTest.getRolePrivileges()).isEqualTo(compare.getRolePrivileges())
        );
    }

    @Test
    void RoleService_updateRole_returnRoleDto() {
        when(roleRepository.findById(any())).thenReturn(Optional.ofNullable(role));
        RoleDto roleTest = roleService.update(1L, updatedRoleDto).orElseThrow();
        RoleDto compare = ConvertEntityToDto.roleToDto(role);
        assertAll(
                () -> assertThat(roleTest).isNotNull(),
                () -> assertThat(roleTest.getName()).isEqualTo(updatedRole.getName()),
                () -> assertThat(roleTest.getRolePrivileges()).isEqualTo(compare.getRolePrivileges())

        );
    }

    @Test
    void RoleService_getByIdRole_returnRoleDto() {
        when(roleRepository.findById(any())).thenReturn(Optional.ofNullable(role));
        RoleDto roleTest = roleService.getById(1L).orElseThrow();
        RoleDto compare = ConvertEntityToDto.roleToDto(role);
        assertAll(
                () -> assertThat(roleTest).isNotNull(),
                () -> assertThat(roleTest.getName()).isEqualTo(role.getName()),
                () -> assertThat(roleTest.getRolePrivileges()).isEqualTo(compare.getRolePrivileges())

        );
    }


    @Test
    void RoleService_getAll_returnListRoleDto() {
        when(roleRepository.findAll()).thenReturn(List.of(role));
        List<RoleDto> roleTest = roleService.getAll();
        verify(roleRepository).findAll();
        assertAll(
                () -> assertThat(roleTest).isNotNull()
        );
    }

    @Test
    void RoleService_delete_returnNon() {
        when(roleRepository.findById(any())).thenReturn(Optional.of(role));
        assertAll(
                () -> roleService.delete(1L)
        );
    }

    @Test
    void RoleService_checkAllMethodsOnThrows_returnThrows() {
        assertAll(
                () -> assertThrows(ResourceNotFoundException.class, () -> roleService.update(1L, updatedRoleDto)),
                () -> assertThrows(ResourceNotFoundException.class, () -> roleService.getById(1L)),
                () -> assertThrows(ResourceNotFoundException.class, () -> roleService.delete(1L))
        );
    }
}

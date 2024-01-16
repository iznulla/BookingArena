package com.booking.arena.service.privilege;

import com.booking.arena.dto.address.CountryDto;
import com.booking.arena.dto.user.PrivilegeDto;
import com.booking.arena.entity.address.CountryEntity;
import com.booking.arena.entity.user.Privilege;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.repository.user.PrivilegeRepository;
import com.booking.arena.service.user.role.PrivilegeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PrivilegeServiceTest {
    @Mock
    PrivilegeRepository privilegeRepository;

    @InjectMocks
    PrivilegeServiceImpl privilegeService;

    PrivilegeDto privilegeDto;
    PrivilegeDto updatedPrivilegeDto;
    Privilege privilege;
    Privilege updatedPrivilege;

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
                .id(1L)
                .name("Update")
                .build();
    }

    @Test
    void PrivilegeService_createPrivilege_returnPrivilegeDto() {
        PrivilegeDto privilegeTest = privilegeService.create(privilegeDto).orElseThrow();
        assertAll(
                () -> assertThat(privilegeTest).isNotNull(),
                () -> assertThat(privilegeTest.getName()).isEqualTo(privilege.getName())
        );
    }

    @Test
    void PrivilegeService_updatePrivilege_returnPrivilegeDto() {
        when(privilegeRepository.findById(any())).thenReturn(Optional.of(privilege));
        PrivilegeDto privilegeTest  = privilegeService.update(1L, updatedPrivilegeDto).orElseThrow();
        verify(privilegeRepository).findById(eq(1L));
        assertAll(
                () -> assertThat(privilegeTest).isNotNull(),
                () -> assertThat(privilegeTest.getName()).isEqualTo(updatedPrivilege.getName())
        );
    }

    @Test
    void PrivilegeService_getByIdPrivilege_returnPrivilegeDto() {
        when(privilegeRepository.findById(any())).thenReturn(Optional.of(privilege));
        PrivilegeDto privilegeTest = privilegeService.getById(1L).orElseThrow();
        verify(privilegeRepository).findById(eq(1L));
        assertAll(
                () -> assertThat(privilegeTest).isNotNull(),
                () -> assertThat(privilegeTest.getName()).isEqualTo(privilegeDto.getName())
        );
    }


    @Test
    void PrivilegeService_getAll_returnListPrivilegeDto() {
        when(privilegeRepository.findAll()).thenReturn(List.of(privilege));
        List<PrivilegeDto> privilegeTest = privilegeService.getAll().orElseThrow();
        verify(privilegeRepository).findAll();
        assertAll(
                () -> assertThat(privilegeTest).isNotNull()
        );
    }

    @Test
    void PrivilegeService_delete_returnNon() {
        when(privilegeRepository.findById(any())).thenReturn(Optional.of(privilege));
        assertAll(
                () -> privilegeService.delete(1L)
        );
    }

    @Test
    void PrivilegeService_checkAllMethodsOnThrows_returnThrows() {
        assertAll(
                () -> assertThrows(ResourceNotFoundException.class, () -> privilegeService.update(1L, updatedPrivilegeDto)),
                () -> assertThrows(ResourceNotFoundException.class, () -> privilegeService.getById(1L)),
                () -> assertThrows(ResourceNotFoundException.class, () -> privilegeService.delete(1L))
        );
    }
}

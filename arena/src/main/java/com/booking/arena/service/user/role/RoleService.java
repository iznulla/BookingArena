package com.booking.arena.service.user.role;



import com.booking.arena.dto.user.RoleDto;
import com.booking.arena.entity.user.RoleEntity;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Optional<RoleDto> getByName(String name);
    Optional<RoleDto> getById(Long id);
    List<RoleDto> getAll();
    Optional<RoleDto> update(Long id, RoleDto roleDto);
    Optional<RoleDto> create(RoleDto roleDto);
    void delete(Long id);
    void deletePrivilege(Long roleId, Long privilegeId);

}

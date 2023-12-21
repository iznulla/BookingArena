package com.booking.arena.service.user.role;



import com.booking.arena.dto.user.RoleDto;
import com.booking.arena.entity.user.RoleEntity;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Optional<RoleEntity> getByName(String name);
    Optional<RoleEntity> getById(Long id);
    List<RoleEntity> getAll();
    Optional<RoleEntity> update(Long id, RoleDto roleDto);
    Optional<RoleEntity> create(RoleDto roleDto);
    void delete(Long id);
    void deletePrivilege(Long roleId, Long privilegeId);

}

package com.booking.arena.service.user.role;



import com.booking.arena.dto.user.PrivilegeDto;
import com.booking.arena.entity.user.Privilege;

import java.util.List;
import java.util.Optional;

public interface PrivilegeService {
    Optional<Privilege> create(PrivilegeDto privilegeDto);
    Optional<Privilege> getById(Long id);
    Optional<Privilege> update(Long id, PrivilegeDto privilegeDto);
    Optional<List<Privilege>> getAll();
    void delete(Long id);
}

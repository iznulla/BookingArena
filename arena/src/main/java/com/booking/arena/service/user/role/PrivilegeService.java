package com.booking.arena.service.user.role;



import com.booking.arena.dto.user.PrivilegeDto;

import java.util.List;
import java.util.Optional;

public interface PrivilegeService {
    Optional<PrivilegeDto> create(PrivilegeDto privilegeDto);
    Optional<PrivilegeDto> getById(Long id);
    Optional<PrivilegeDto> update(Long id, PrivilegeDto privilegeDto);
    Optional<List<PrivilegeDto>> getAll();
    void delete(Long id);
}

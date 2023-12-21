package com.booking.arena.repository;


import com.booking.arena.entity.user.RolePrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege, Long> {
    Optional<RolePrivilege> findByPrivilegeIdAndRoleId(Long privilegeId, Long roleId);
}

package com.booking.arena.service.user.role;

import com.booking.arena.dto.user.PrivilegeDto;
import com.booking.arena.dto.user.RoleDto;
import com.booking.arena.entity.user.Privilege;
import com.booking.arena.entity.user.RoleEntity;
import com.booking.arena.entity.user.RolePrivilege;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.repository.PrivilegeRepository;
import com.booking.arena.repository.RolePrivilegeRepository;
import com.booking.arena.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final RolePrivilegeRepository rolePrivilegeRepository;


    @Override
    public Optional<RoleEntity> getByName(String name) {
        return Optional.of(roleRepository.findByName(name).orElseThrow(() ->
                new ResourceNotFoundException("Not found role with name: " + name)));
    }

    @Override
    public Optional<RoleEntity> getById(Long id) {
        return Optional.of(roleRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Not found role with id: " + id)));
    }

    @Override
    public List<RoleEntity> getAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<RoleEntity> update(Long id, RoleDto roleDto) {
        RoleEntity role = roleRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Not found role with id: " + id));
        role.setName(roleDto.getName());
        for (PrivilegeDto p : roleDto.getPrivileges()) {
            Privilege privilege = privilegeRepository.findByName(p.getName()).orElseThrow(() ->
                    new ResourceNotFoundException("Not found privilege with name: " + p.getName()));
            RolePrivilege rolePrivilege = new RolePrivilege();
            rolePrivilege.setPrivilege(privilege);
            rolePrivilege.setRole(role);
        }
        role.setUpdatedAt(Instant.now());
        roleRepository.save(role);
        return Optional.of(role);
    }

    @Override
    public Optional<RoleEntity> create(RoleDto roleDto) {
        RoleEntity role = new RoleEntity();
        for (PrivilegeDto p : roleDto.getPrivileges()) {
            Privilege privilege = privilegeRepository.findByName(p.getName()).orElseThrow(() ->
                    new ResourceNotFoundException("Not found privilege with name: " + p.getName()));
            RolePrivilege rolePrivilege = new RolePrivilege();
            rolePrivilege.setPrivilege(privilege);
            rolePrivilege.setRole(role);
        }
        role.setName(roleDto.getName());
        role.setCreatedAt(Instant.now());
        role.setUpdatedAt(Instant.now());
        roleRepository.save(role);
        return Optional.of(role);
    }

    @Override
    public void delete(Long id) {
        if (roleRepository.findById(id).isPresent()) {
            roleRepository.deleteById(id);
            log.info("Role deleted by id: {}", id);
        } else {
            throw new ResourceNotFoundException("Not found role with id: " + id);
        }
    }

    @Override
    public void deletePrivilege(Long roleId, Long privilegeId) {
        RolePrivilege rolePrivilege = rolePrivilegeRepository.findByPrivilegeIdAndRoleId(roleId, privilegeId).orElseThrow(() ->
                new ResourceNotFoundException("Not found rolePrivilege with privilegeId: " + privilegeId + " and roleId: " + roleId));
        rolePrivilegeRepository.delete(rolePrivilege);
        log.info("deleted privilege by Id: {} from role by Id: {}", privilegeId, roleId);
    }
}

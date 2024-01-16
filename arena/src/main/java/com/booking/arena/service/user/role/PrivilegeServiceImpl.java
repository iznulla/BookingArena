package com.booking.arena.service.user.role;

import com.booking.arena.dto.user.PrivilegeDto;
import com.booking.arena.entity.user.Privilege;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.repository.user.PrivilegeRepository;
import com.booking.arena.utils.ConvertEntityToDto;
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
public class PrivilegeServiceImpl implements PrivilegeService {
    private final PrivilegeRepository privilegeRepository;

    @Override
    public Optional<PrivilegeDto> create(PrivilegeDto privilegeDto) {
        try {
            Privilege privilege = Privilege.builder()
                    .name(privilegeDto.getName())
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();
            privilegeRepository.save(privilege);
            log.info("Privilege created: {}", privilegeDto.getName());
            return Optional.of(ConvertEntityToDto.privilegeToDto(privilege));
        } catch (Exception e) {
            throw new ResourceNotFoundException("Invalid privilege details\n" + e.getMessage());
        }

    }

    @Override
    public Optional<PrivilegeDto> getById(Long id) {
        return Optional.of(ConvertEntityToDto.privilegeToDto(privilegeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not found privilege with id: " + id)
        )));
    }

    @Override
    public Optional<PrivilegeDto> update(Long id, PrivilegeDto privilegeDto) {
        Privilege privilege = privilegeRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Not found privilege with id: " + id));
        try {
            privilege.setName(privilegeDto.getName());
            privilege.setUpdatedAt(Instant.now());
            privilegeRepository.save(privilege);
            log.info("Privilege updated: {}", privilegeDto.getName());
            return Optional.of(ConvertEntityToDto.privilegeToDto(privilege));
        } catch (Exception e) {
            throw new ResourceNotFoundException("Invalid privilege details\n" + e.getMessage());
        }
    }

    @Override
    public Optional<List<PrivilegeDto>> getAll() {
        return Optional.of(privilegeRepository.findAll().stream().map(ConvertEntityToDto::privilegeToDto).toList());
    }

    @Override
    public void delete(Long id) {
        if (privilegeRepository.findById(id).isPresent()) {
            privilegeRepository.deleteById(id);
            log.info("Privilege deleted by id: {}", id);

        } else {
            throw new ResourceNotFoundException("Not found privilege with id: " + id);
        }
    }
}

package com.booking.arena.service.arena;

import com.booking.arena.dto.arena.ArenaDto;
import com.booking.arena.entity.arena.ArenaEntity;
import com.booking.arena.entity.arena.ArenaInfo;
import com.booking.arena.entity.user.UserEntity;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.repository.arena.ArenaRepository;
import com.booking.arena.repository.user.UserRepository;
import com.booking.arena.utils.ConvertEntityToDto;
import com.booking.arena.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ArenaServiceImpl implements ArenaService{
    private final ArenaRepository arenaRepository;
    private final ArenaInfoService arenaInfoService;
    private final UserRepository userRepository;

    @Override
    public Optional<ArenaDto> getById(Long id) {
        ArenaEntity arena = arenaRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Not found arena with id: " + id));
        return Optional.of(ConvertEntityToDto.arenaToDto(arena));
    }

    @Override
    public List<ArenaDto> getAll() {
        List<ArenaEntity> arenas = arenaRepository.findAll();
        return Optional.of(arenas.stream().map(ConvertEntityToDto::arenaToDto).toList()).orElseThrow(() ->
                new ResourceNotFoundException("Not found any arena")
        );
    }

    @Override
    public Optional<ArenaDto> create(ArenaDto arenaDto) {
        try {
            ArenaInfo arenaInfo = arenaInfoService.create(arenaDto.getArenaInfo()).orElseThrow(
                    () -> new ResourceNotFoundException("ArenaInfo is Not valid")
            );
            UserEntity user = userRepository.findById(SecurityUtils.getCurrentUserId()).orElseThrow(
                    () -> new ResourceNotFoundException("User is Not valid")
            );
            ArenaEntity arena = ArenaEntity.builder()
                    .arenaInfo(arenaInfo)
                    .name(arenaDto.getName())
                    .description(arenaDto.getDescription())
                    .user(user)
                    .status(true)
                    .build();
            arenaInfo.setArena(arena);
            arenaRepository.save(arena);
            log.debug("Create arena with id: {}, from user by username: {}", arena.getId(), user.getUsername());
            return Optional.of(ConvertEntityToDto.arenaToDto(arena));
        } catch (Exception e) {
            throw new ResourceNotFoundException("Invalid input data\n" + e.getMessage());
        }
    }

    @Override
    public Optional<ArenaDto> update(Long id, ArenaDto arenaDto) {
        ArenaEntity arena = arenaRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Not found arena with id: " + id));
        try {
            arena.setName(arenaDto.getName());
            arena.setDescription(arenaDto.getDescription());
            arena.setArenaInfo(arenaInfoService.update(arena.getArenaInfo().getId(), arenaDto.getArenaInfo()).orElseThrow());
            arenaRepository.save(arena);
            log.debug("Updated arena with id: {}, from user by username: {}", arena.getId(), SecurityUtils.getCurrentUsername());
            return Optional.of(ConvertEntityToDto.arenaToDto(arena));
        } catch (Exception e) {
            throw new ResourceNotFoundException("Invalid input data");
        }
    }

    @Override
    public void delete(Long id) {
        ArenaEntity arena = arenaRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Not found arena with id: " + id));
        arenaRepository.deleteById(arena.getId());
        log.debug("Deleted arena with id: {}, from user by username: {}", arena.getId(), SecurityUtils.getCurrentUsername());
    }
}
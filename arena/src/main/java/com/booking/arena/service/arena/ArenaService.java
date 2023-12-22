package com.booking.arena.service.arena;

import com.booking.arena.dto.arena.ArenaDto;

import java.util.List;
import java.util.Optional;

public interface ArenaService {
    Optional<ArenaDto> getById(Long id);
    List<ArenaDto> getAll();
    Optional<ArenaDto> create(ArenaDto arenaDto);
    Optional<ArenaDto> update(Long id, ArenaDto arenaDto);
    void delete(Long id);
}

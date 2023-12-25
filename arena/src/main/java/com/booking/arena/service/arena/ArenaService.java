package com.booking.arena.service.arena;

import com.booking.arena.dto.arena.ArenaDto;
import com.booking.arena.dto.arena.ArenaFiltersDto;
import com.booking.arena.entity.arena.ArenaEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ArenaService {
    Optional<ArenaDto> getById(Long id);
    List<ArenaDto> getAll();
    List<ArenaDto> getByFilter(ArenaFiltersDto filters);
    Optional<ArenaDto> create(ArenaDto arenaDto);
    Optional<ArenaDto> update(Long id, ArenaDto arenaDto);
    void delete(Long id);
}

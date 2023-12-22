package com.booking.arena.service.arena;

import com.booking.arena.dto.arena.ArenaInfoDto;
import com.booking.arena.entity.arena.ArenaInfo;

import java.util.Optional;

public interface ArenaInfoService {
    Optional<ArenaInfo> getById(Long id);
    Optional<ArenaInfo> create(ArenaInfoDto arenaInfoDto);
    Optional<ArenaInfo> update(Long id, ArenaInfoDto arenaInfoDto);
}

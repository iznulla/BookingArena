package com.booking.arena.repository.arena;

import com.booking.arena.entity.arena.ArenaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArenaRepository extends JpaRepository<ArenaEntity, Long> {
}

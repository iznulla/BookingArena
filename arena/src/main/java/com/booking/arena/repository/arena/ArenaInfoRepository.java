package com.booking.arena.repository.arena;

import com.booking.arena.entity.arena.ArenaInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArenaInfoRepository extends JpaRepository<ArenaInfo, Long> {
}

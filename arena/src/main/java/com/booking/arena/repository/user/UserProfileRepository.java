package com.booking.arena.repository.user;

import com.booking.arena.entity.user.UserEntity;
import com.booking.arena.entity.user.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUser(UserEntity user);
}

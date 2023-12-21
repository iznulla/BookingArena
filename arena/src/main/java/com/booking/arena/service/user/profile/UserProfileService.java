package com.booking.arena.service.user.profile;

import com.booking.arena.dto.user.UserProfileDto;

import java.util.Optional;

public interface UserProfileService {
    Optional<UserProfileDto> getUserProfileByUserId(Long id);
    Optional<UserProfileDto> updateUserProfile(UserProfileDto userProfileDto);

}

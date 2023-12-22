package com.booking.arena.service.user.profile;

import com.booking.arena.dto.user.UserProfileDto;
import com.booking.arena.entity.address.Address;
import com.booking.arena.entity.user.UserEntity;
import com.booking.arena.entity.user.UserProfile;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.repository.user.UserProfileRepository;
import com.booking.arena.repository.user.UserRepository;
import com.booking.arena.service.address.AddressService;
import com.booking.arena.utils.ConvertEntityToDto;
import com.booking.arena.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final AddressService addressService;
    @Override
    public Optional<UserProfileDto> getUserProfileByUserId(Long id) {
        UserProfile profile = userProfileRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Not found profile with id: " + id));
        return Optional.of(ConvertEntityToDto.userProfileToDto(profile));
    }

    @Override
    public Optional<UserProfileDto> updateUserProfile(UserProfileDto userProfileDto) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        UserEntity user = userRepository.findById(currentUserId).orElseThrow(() ->
                new ResourceNotFoundException("Not found user with id: " + currentUserId));
        UserProfile profile = userProfileRepository.findByUser(user).orElseThrow();

        user.setUsername(userProfileDto.getUsername());
        user.setEmail(userProfileDto.getEmail());

        profile.setName(userProfileDto.getName());
        profile.setSurname(userProfileDto.getSurname());
        profile.setUpdatedAt(Instant.now());
        Address address = addressService.getAddressById(profile.getAddress().getId()).orElseThrow(
                () -> new ResourceNotFoundException("Not found address with id: " + profile.getAddress().getId())
        );
        addressService.updateAddress(address.getId(), userProfileDto.getAddress()).orElseThrow();
        userProfileRepository.save(profile);
        userRepository.save(user);
        return Optional.of(ConvertEntityToDto.userProfileToDto(profile));
    }

}

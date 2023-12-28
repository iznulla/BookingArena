package com.booking.arena.configuration;

import com.booking.arena.entity.address.Address;
import com.booking.arena.entity.user.*;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.repository.user.PrivilegeRepository;
import com.booking.arena.repository.user.RoleRepository;
import com.booking.arena.repository.user.UserRepository;
import com.booking.arena.service.address.AddressService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class ProjectConfig {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AddressService addressService;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    private void initDefaultUsers() {
        if (userRepository.findByUsername("admin").isPresent())
            return;

        RoleEntity role = roleRepository.findByName("ADMIN").orElseThrow(
                () -> new ResourceNotFoundException("Role ADMIN not found")
        );
        roleRepository.save(role);
        Address address = addressService.getById(10L).orElseThrow(
                () -> new ResourceNotFoundException("Address not found")
        );

        UserEntity admin = UserEntity.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .email("admin@mail.ru")
                .role(role)
                .isActive(true)
                .build();
        UserProfile userProfile = UserProfile
                .builder()
                .user(admin)
                .address(address)
                .name("admin")
                .surname("admin")
                .build();
        admin.setUserProfile(userProfile);
        userRepository.save(admin);
    }
}

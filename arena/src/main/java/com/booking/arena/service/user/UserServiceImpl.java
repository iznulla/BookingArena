package com.booking.arena.service.user;

import com.booking.arena.dto.auth.SignUpDto;
import com.booking.arena.dto.user.update.UserUpdateDto;
import com.booking.arena.dto.user.UserDto;
import com.booking.arena.entity.address.Address;
import com.booking.arena.entity.user.RoleEntity;
import com.booking.arena.entity.user.UserEntity;
import com.booking.arena.entity.user.UserProfile;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.repository.user.RoleRepository;
import com.booking.arena.repository.user.UserRepository;
import com.booking.arena.service.address.AddressService;
import com.booking.arena.service.email.EmailServiceImpl;
import com.booking.arena.service.email.EmailVerificationService;
import com.booking.arena.utils.ConvertEntityToDto;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Data
@Builder
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressService addressService;
    private final EmailServiceImpl emailService;
    private final EmailVerificationService emailVerificationService;
    private final RoleRepository roleRepository;

    @Override
    public Optional<UserDto> create(SignUpDto signUpDto) {
        RoleEntity role = roleRepository.findByName("USER").orElseThrow(
                () -> new ResourceNotFoundException("No correspond to any role")
        );
        UserEntity user = UserEntity.builder()
                .username(signUpDto.getUsername())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .role(role)
                .email(signUpDto.getEmail())
                .build();
        Address address = addressService.create(signUpDto.getUserProfile().getAddress()).orElseThrow(
                () -> new ResourceNotFoundException("No correspond to any address")
        );
        UserProfile profile = UserProfile.builder()
                .user(user)
                .name(signUpDto.getUserProfile().getName())
                .surname(signUpDto.getUserProfile().getSurname())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        profile.setAddress(address);
        user.setUserProfile(profile);
        userRepository.save(user);
        emailService.send(signUpDto.getEmail(), "Verify Code", emailVerificationService.generateCode());
        log.debug("Created user by name {}", signUpDto.getUserProfile().getName());
        return Optional.of(Optional.of(ConvertEntityToDto.userToDto(user)).orElseThrow());
    }


    @Override
    public List<UserDto> getAll() {
        return Optional.of(userRepository.findAll().stream().map(ConvertEntityToDto::userToDto).toList()).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Users not found"
                )
        );
    }

    @Override
    public Optional<UserDto> getUserByUsername(String username) {
        return Optional.ofNullable(ConvertEntityToDto.userToDto(userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("User with username %s not found", username)))));
    }

    @Override
    public Optional<UserDto> getById(Long id) {
        return Optional.ofNullable(ConvertEntityToDto.userToDto(userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("User with id %s not found", id)
                )
        )));
    }

    @Override
    public Optional<UserDto> update(Long id, UserUpdateDto userDto) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("User with id %s not found", id)
        ));

        RoleEntity role = roleRepository.findByName(userDto.getRole()).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Role with name %s not found", userDto.getRole())
        ));
        user.setRole(role);
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setActive(userDto.isActive());
        userRepository.save(user);
        log.warn("Updated user by id {}", id);
        return Optional.of(ConvertEntityToDto.userToDto(user));
    }

    @Override
    public void delete(Long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            log.debug("deleted user by id {}", id);
        } else {
            throw new ResourceNotFoundException(
                    String.format("User with id %s not found", id));
        }
    }
    @Override
    public boolean verification(Long id, String code) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("User with id %s not found", id)
        ));
        if (user.isActive()) {
            return false;
        }
        Instant now = Instant.now();
        try {
            if (!now.isBefore(emailVerificationService.getExpiredDate())) {
                return false;
        }
        } catch (Exception e) {
            throw new ResourceNotFoundException(
                "verification time expired"
            );
        }
        if (!code.equals(emailVerificationService.getVerifyCode())) {
            return false;
        }
        user.setActive(true);
        userRepository.save(user);
        log.debug("User verified by id {}", id);
        return true;
    }

    public void resendVerificationCode(Long id) {
        emailService.send(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("User with id %s not found", id)
        )).getEmail(), "Verify Code", emailVerificationService.generateCode());
    }
}

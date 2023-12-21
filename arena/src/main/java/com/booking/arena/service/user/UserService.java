package com.booking.arena.service.user;

import com.booking.arena.dto.auth.SignUpDto;
import com.booking.arena.dto.user.update.UserUpdateDto;
import com.booking.arena.dto.user.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserDto> createUser(SignUpDto signUpDto);
    List<UserDto> findAllUsers();
    Optional<UserDto> getUserByUsername(String username);
    Optional<UserDto> getUserById(Long id);
    Optional<UserDto> updateUser(Long id, UserUpdateDto userDto);
    void deleteUser(Long id);
//    boolean verification(Long id, String code);
}

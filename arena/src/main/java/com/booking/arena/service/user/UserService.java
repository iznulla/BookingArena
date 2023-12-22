package com.booking.arena.service.user;

import com.booking.arena.dto.auth.SignUpDto;
import com.booking.arena.dto.user.update.UserUpdateDto;
import com.booking.arena.dto.user.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserDto> create(SignUpDto signUpDto);
    List<UserDto> getAll();
    Optional<UserDto> getUserByUsername(String username);
    Optional<UserDto> getById(Long id);
    Optional<UserDto> update(Long id, UserUpdateDto userDto);
    void delete(Long id);
//    boolean verification(Long id, String code);
}

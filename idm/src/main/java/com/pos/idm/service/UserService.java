package com.pos.idm.service;

import com.pos.idm.entity.UserEntity;
import com.pos.idm.exception.BadCredentialsException;
import com.pos.idm.exception.UserNotFoundException;
import com.pos.idm.soap.dto.LoginRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserEntity findUserByCredentials(LoginRequest request) throws BadCredentialsException;

    void addUser(UserEntity user);

    UserEntity findUserById(Integer id) throws UserNotFoundException;

    List<UserEntity> findAllUsers();

    void updateUser(UserEntity user);

    void deleteUserById(Integer id);

    Optional<UserEntity> findUserByUsername(String username);
}

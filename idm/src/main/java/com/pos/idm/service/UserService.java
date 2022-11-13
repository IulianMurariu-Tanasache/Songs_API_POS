package com.pos.idm.service;

import com.pos.idm.entity.UserEntity;
import com.pos.idm.exception.BadCredentialsException;
import com.pos.idm.soap.dto.AuthRequest;

public interface UserService {
    UserEntity findUserByCredentials(AuthRequest request) throws BadCredentialsException;

    boolean checkUsernameExists(String username) throws BadCredentialsException;

    void addUser(UserEntity user);
}

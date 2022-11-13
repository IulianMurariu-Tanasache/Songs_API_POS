package com.pos.idm.service;

import com.pos.idm.entity.UserEntity;
import com.pos.idm.exception.InvalidJWTException;

import java.util.Set;

public interface JwtService {
    String generateJwt(UserEntity user);

    Set<String> validateJwt(String jwt) throws InvalidJWTException;

    String getUsernameFromJWT(String jwt) throws InvalidJWTException;
}

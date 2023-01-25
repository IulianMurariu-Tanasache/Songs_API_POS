package com.pos.idm.service;

import com.pos.idm.entity.UserEntity;
import com.pos.idm.exception.InvalidJWTException;

import java.util.List;

public interface JwtService {
    String generateJwt(UserEntity user);

    List<String> validateJwt(String jwt) throws InvalidJWTException;

    String getUsernameFromJWT(String jwt) throws InvalidJWTException;

    void invalidateToken(String jwt) throws InvalidJWTException;
}

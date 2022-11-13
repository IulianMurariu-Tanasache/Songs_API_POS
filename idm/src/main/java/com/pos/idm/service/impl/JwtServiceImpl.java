package com.pos.idm.service.impl;

import com.pos.idm.entity.RoleEntity;
import com.pos.idm.entity.UserEntity;
import com.pos.idm.exception.InvalidJWTException;
import com.pos.idm.jwt.JwtHelper;
import com.pos.idm.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class JwtServiceImpl implements JwtService {

    private final JwtHelper jwtHelper;

    @Override
    public String generateJwt(UserEntity user) {
        Set<String> authorities = user.getRoleEntities()
                                        .stream()
                                        .map(RoleEntity::getRole)
                                        .collect(Collectors.toSet());

        return jwtHelper.generateJwt(user.getUsername(), authorities);
    }

    @Override
    public Set<String> validateJwt(String jwt) throws InvalidJWTException {
        List<Map<String, String>> authorities =  jwtHelper.verifyToken(jwt);
        return new HashSet<>();
    }

    @Override
    public String getUsernameFromJWT(String jwt) throws InvalidJWTException {
        return jwtHelper.getJWTBody(jwt).getSubject();
    }
}

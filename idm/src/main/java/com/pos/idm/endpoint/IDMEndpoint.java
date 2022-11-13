package com.pos.idm.endpoint;

import com.pos.idm.entity.UserEntity;
import com.pos.idm.exception.BadCredentialsException;
import com.pos.idm.exception.InvalidJWTException;
import com.pos.idm.service.JwtService;
import com.pos.idm.service.UserService;
import com.pos.idm.soap.dto.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Endpoint
public class IDMEndpoint {

    private final String NAMESPACE = "http://techprimers.com/spring-boot-soap-example";
    private final UserService userService;
    private final JwtService jwtService;
    private final ModelMapper mapper;

    @PayloadRoot(namespace = NAMESPACE, localPart = "authRequest")
    @ResponsePayload
    public AuthResponse authRequest(@RequestPayload AuthRequest authRequest) throws BadCredentialsException {
        UserEntity user = userService.findUserByCredentials(authRequest);
        AuthResponse response = new AuthResponse();
        response.setJwt(jwtService.generateJwt(user));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "jwtVerifyRequest")
    @ResponsePayload
    public JwtVerifyResponse jwtVerifyRequest(@RequestPayload JwtVerifyRequest jwtVerifyRequest) throws InvalidJWTException {
        Set<String> authorities = jwtService.validateJwt(jwtVerifyRequest.getJwt());
        String username = jwtService.getUsernameFromJWT(jwtVerifyRequest.getJwt());
       try {
           if(userService.checkUsernameExists(username)) {
               JwtVerifyResponse response = new JwtVerifyResponse();
               response.setUsername(username);
               response.setRoles(new ArrayList<>(authorities));
               return response;
           }
       } catch (BadCredentialsException e) {
           throw new InvalidJWTException();
       }
       return null;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "userRequest")
    //@ResponsePayload
    public void createUser(@RequestPayload UserRequest userRequest) {
        userService.addUser(mapper.map(userRequest, UserEntity.class));
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "userRequest")
    @ResponsePayload
    public UserResponse updateUser(@RequestPayload UserRequest userRequest) {

    }
}

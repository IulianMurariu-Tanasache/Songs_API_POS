package com.pos.idm.endpoint;

import com.pos.idm.entity.RoleEntity;
import com.pos.idm.entity.UserEntity;
import com.pos.idm.exception.BadCredentialsException;
import com.pos.idm.exception.InvalidJWTException;
import com.pos.idm.exception.UserNotFoundException;
import com.pos.idm.service.JwtService;
import com.pos.idm.service.RoleService;
import com.pos.idm.service.UserService;
import com.pos.idm.soap.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Endpoint
public class IDMEndpoint {
    private final UserService userService;
    private final RoleService roleService;
    private final JwtService jwtService;

    @PayloadRoot(namespace = "idm_namespace", localPart = "loginRequest")
    @ResponsePayload
    public LoginResponse loginRequest(@RequestPayload LoginRequest loginRequest) throws BadCredentialsException {
        UserEntity user = userService.findUserByCredentials(loginRequest);
        LoginResponse response = new LoginResponse();
        response.setId(user.getId());
        response.setJwt(jwtService.generateJwt(user));
        return response;
    }

    @PayloadRoot(namespace = "idm_namespace", localPart = "authRequest")
    @ResponsePayload
    public AuthResponse authRequest(@RequestPayload AuthRequest authRequest) throws InvalidJWTException {
        List<String> authorities = jwtService.validateJwt(authRequest.getJwt());
        String username = jwtService.getUsernameFromJWT(authRequest.getJwt());
        Optional<UserEntity> user = userService.findUserByUsername(username);
       if(user.isPresent()) {
           AuthResponse response = new AuthResponse();
           response.setId(user.get().getId());
           response.setRoles(authorities);
           return response;
       } else {
           throw new InvalidJWTException();
       }
    }

    @PayloadRoot(namespace = "idm_namespace", localPart = "createUserRequest")
    public void createUser(@RequestPayload CreateUserRequest userRequest) throws RoleNotFoundException {
        UserEntity userEntity = new UserEntity();
        System.out.println(userRequest.getRoles());
        userEntity.setUsername(userRequest.getUsername());
        userEntity.setPassword(userRequest.getPassword());
        userEntity.setRoleEntities(roleService.findRolesByName(userRequest.getRoles()));
        userService.addUser(userEntity);
    }

    @PayloadRoot(namespace = "idm_namespace", localPart = "getUserRequest")
    @ResponsePayload
    public GetUserResponse getUserRequest(@RequestPayload GetUserRequest getUserRequest) throws UserNotFoundException {
        UserEntity user = userService.findUserById(getUserRequest.getId());
        System.out.println(user);
        GetUserResponse response = new GetUserResponse();
        response.setUsername(user.getUsername());
        response.setPassword(user.getPassword());
        response.setRoles(
            user.getRoleEntities().stream()
                .map(RoleEntity::getRole)
                .collect(Collectors.toList())
        );
        return response;
    }

    @PayloadRoot(namespace = "idm_namespace", localPart = "getUsersRequest")
    @ResponsePayload
    public GetUsersResponse getUsersRequest() {
        GetUsersResponse getUsersResponse = new GetUsersResponse();
        getUsersResponse.setUsers(
                userService.findAllUsers().stream()
                        .map(user -> {
                            User userDTO = new User();
                            userDTO.setUsername(user.getUsername());
                            userDTO.setPassword(user.getPassword());
                            userDTO.setRoles(
                                    user.getRoleEntities().stream()
                                            .map(RoleEntity::getRole)
                                            .collect(Collectors.toList())
                            );
                            return userDTO;
                        })
                        .collect(Collectors.toList())
        );
        return getUsersResponse;
    }
    @PayloadRoot(namespace = "idm_namespace", localPart = "updateUserRequest")
    public void updateUserRequest(@RequestPayload UpdateUserRequest updateUserRequest) throws RoleNotFoundException {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(updateUserRequest.getId());
        userEntity.setUsername(updateUserRequest.getUsername());
        userEntity.setPassword(updateUserRequest.getPassword());
        userEntity.setRoleEntities(roleService.findRolesByName(updateUserRequest.getRoles()));

        userService.updateUser(userEntity);
    }

    @PayloadRoot(namespace = "idm_namespace", localPart = "deleteUserRequest")
    public void deleteUserRequest(@RequestPayload DeleteUserRequest deleteUserRequest) {
        userService.deleteUserById(deleteUserRequest.getId());
    }

    @PayloadRoot(namespace = "idm_namespace", localPart = "logoutRequest")
    public void logoutRequest(@RequestPayload LogoutRequest logoutRequest) throws InvalidJWTException {
        try {
            jwtService.invalidateToken(logoutRequest.getJwt());
        } catch (InvalidJWTException e) {
            throw new InvalidJWTException();
        }
    }
}

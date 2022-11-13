package com.pos.gateway.controller;

import com.pos.commons.idm.UserDTO;
import com.pos.gateway.config.ServiceDiscoveryProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Set;

@RequiredArgsConstructor
@RestController
public class IDMController {

    private final ServiceDiscoveryProperties servicesProperties;
    private final WebClient webClient;
    private String makeUri(String host, int port, String path) {
        return "http://" + host + ":" + port + "/" + path;
    }

    @PostMapping("/users")
    public ResponseEntity<Void> createUser(@RequestBody UserDTO user) {
        webClient.post()
                .uri(makeUri(servicesProperties.getIdmHost(), servicesProperties.getIdmPort(), "/users"))
                .bodyValue(user)
                .retrieve()
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody UserDTO user) {
        User userToSave = mapper.map(user, User.class);
        Set<Role> roles = Set.of(roleService.findRoleByName("ROLE_CLIENT").orElseThrow());
        userToSave.setRoles(roles);
        userService.save(userToSave);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/users")
    public ResponseEntity<Void> deleteUser(@RequestHeader("Authorization") String jwt, @RequestParam("username") String username) {
        UserDTO user = null;
        try {
            user = jwtService.verifyJwt(jwt);
            if(!username.equals(user.getUsername())) {
                throw new IllegalStateException();
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        userService.delete(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/users")
    public ResponseEntity<Void> updateUser(@RequestBody UserDTO userDTO, @RequestParam("username") String username) {
        User userToUpdate = userService.findUserByUsername(username).orElseThrow();
        User newUserDetails = mapper.map(userDTO, User.class);
        newUserDetails.setId(userToUpdate.getId());
        userService.delete(username);
        userService.save(newUserDetails);
        return ResponseEntity.ok().build();
    }
}

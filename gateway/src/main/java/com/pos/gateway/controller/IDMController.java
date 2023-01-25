package com.pos.gateway.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pos.gateway.service.IdmConsumer;
import com.pos.gateway.soap.*;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class IDMController {

    private final IdmConsumer idmConsumer;

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<GetUserResponse>>getUserById(@PathVariable Integer id) {
        try {
            return ResponseEntity.of(
                    Optional.of(idmConsumer.getUserById(id))
                            .map(EntityModel::of)
                            .map(model -> model.add(WebMvcLinkBuilder.linkTo(IDMController.class).slash(id).withSelfRel()))
                            .map(model -> model.add(WebMvcLinkBuilder.linkTo(IDMController.class).withRel("parent")))
                            .map(model -> model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PlaylistGatewayController.class).getAllPlaylists(null,null,id,null,null)).withRel("playlists")))
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

   @GetMapping
   public ResponseEntity<GetUsersResponse> getUsers() {
       try {
           return ResponseEntity.ok(idmConsumer.getUsers());
       } catch (JsonProcessingException e) {
           throw new RuntimeException(e);
       }
   }

   @PreAuthorize("hasRole('ROLE_ADMIN')")
   @PostMapping
   public ResponseEntity<Void> addUser(@RequestBody CreateUserRequest createUserRequest) {
       try {
           idmConsumer.createUser(createUserRequest);
           return ResponseEntity.noContent().build();
       } catch (JsonProcessingException e) {
           throw new RuntimeException(e);
       }
   }

   @PreAuthorize("hasRole('ROLE_ADMIN')")
   @PutMapping("/{id}")
   public ResponseEntity<Void> addOrModifyUser(@RequestBody UpdateUserRequest updateUserRequest, @PathVariable Integer id) {
       try {
           updateUserRequest.setId(id);
           idmConsumer.updateUser(updateUserRequest);
           return ResponseEntity.noContent().build();
       } catch (JsonProcessingException e) {
           throw new RuntimeException(e);
       }
   }

   @PreAuthorize("hasRole('ROLE_ADMIN')")
   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
       try {
           DeleteUserRequest request = new DeleteUserRequest();
           request.setId(id);
           idmConsumer.deleteUser(request);
           return ResponseEntity.noContent().build();
       } catch (JsonProcessingException e) {
           throw new RuntimeException(e);
       }
   }

   @GetMapping("/logout")
    public ResponseEntity<Void> invalidateToken(@RequestHeader String Authorization) {
       System.out.println(Authorization);
       LogoutRequest logoutRequest = new LogoutRequest();
       logoutRequest.setJwt(Authorization.replaceAll("Bearer ", ""));
       try {
           idmConsumer.sendLogoutRequest(logoutRequest);
           return ResponseEntity.noContent().build();
       } catch (JsonProcessingException e) {
           throw new RuntimeException(e);
       }
   }
}

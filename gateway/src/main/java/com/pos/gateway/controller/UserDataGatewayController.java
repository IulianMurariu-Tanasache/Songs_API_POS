package com.pos.gateway.controller;

import com.pos.commons.api.UserDataAPI;
import com.pos.commons.dto.UserDataDTO;
import com.pos.gateway.config.ServiceDiscoveryProperties;
import com.pos.gateway.util.URLHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserDataGatewayController implements UserDataAPI {

    private final ServiceDiscoveryProperties discoveryProperties;
    private final WebClient webClient;

    @Override
    public ResponseEntity<List<UserDataDTO>> getAllUserData(String name, String username, String email, Integer page, Integer itemsPerPage) {
        List<UserDataDTO> userDataList =
                webClient.get().uri(discoveryProperties.getUserDataServiceLocation() + URLHelper.getFullPath())
                        .retrieve()
                        .bodyToFlux(UserDataDTO.class)
                        .timeout(Duration.of(1000, ChronoUnit.SECONDS))
                        .collect(Collectors.toList()).block();

        return ResponseEntity.ok(
                userDataList.stream()
                    .map(userData -> userData.add(WebMvcLinkBuilder.linkTo(UserDataGatewayController.class).slash((userData).getUserId()).withSelfRel()))
                    .map(userData -> userData.add(WebMvcLinkBuilder.linkTo(PlaylistGatewayController.class).slash(((UserDataDTO)userData).getUserId()).withRel("playlists")))
                    .map(userData -> (UserDataDTO)userData)
                    .collect(Collectors.toList())
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #userDataDTO.userId == authentication.principal")
    @Override
    public ResponseEntity<Void> addUserData(UserDataDTO userDataDTO) {
        return webClient.post().uri(discoveryProperties.getUserDataServiceLocation() + URLHelper.getFullPath())
                .bodyValue(userDataDTO)
                .exchangeToMono(clientResponse -> Objects.requireNonNull(clientResponse.toEntity(Void.class)))
                .timeout(Duration.of(1000, ChronoUnit.SECONDS))
                .block();
    }

    @Override
    public ResponseEntity<UserDataDTO> getUserDataById(Integer id) {
        Optional<UserDataDTO> optionalUserData = Optional.ofNullable(
                webClient.get().uri(discoveryProperties.getUserDataServiceLocation() + URLHelper.getFullPath())
                .exchangeToMono(clientResponse -> Objects.requireNonNull(clientResponse.toEntity(UserDataDTO.class)))
                .timeout(Duration.of(1000, ChronoUnit.SECONDS))
                .block().getBody());

        return ResponseEntity.of(
                optionalUserData
                    .map(userData -> userData.add(WebMvcLinkBuilder.linkTo(UserDataGatewayController.class).slash(userData.getUserId()).withSelfRel()))
                    .map(userData -> userData.add(WebMvcLinkBuilder.linkTo(UserDataGatewayController.class).withRel("parent")))
                    .map(userData -> userData.add(WebMvcLinkBuilder.linkTo(PlaylistGatewayController.class).slash(((UserDataDTO)userData).getUserId()).withRel("playlists")))
                    .map(userData -> (UserDataDTO)userData)
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #userDataDTO.userId == authentication.principal")
    @Override
    public ResponseEntity<Void> addOrUpdateUserData(Integer id, UserDataDTO userDataDTO) {
        return webClient.put().uri(discoveryProperties.getUserDataServiceLocation() + URLHelper.getFullPath())
                .bodyValue(userDataDTO)
                .exchangeToMono(clientResponse -> Objects.requireNonNull(clientResponse.toEntity(Void.class)))
                .timeout(Duration.of(1000, ChronoUnit.SECONDS))
                .block();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<Void> deleteUserDataById(Integer id) {
        return webClient.delete().uri(discoveryProperties.getUserDataServiceLocation() + URLHelper.getFullPath())
                .exchangeToMono(clientResponse ->Objects.requireNonNull(clientResponse.toEntity(Void.class)))
                .timeout(Duration.of(1000, ChronoUnit.SECONDS))
                .block();
    }
}

package com.pos.gateway.controller;

import com.pos.commons.api.PlaylistAPI;
import com.pos.commons.dto.PlaylistDTO;
import com.pos.gateway.config.ServiceDiscoveryProperties;
import com.pos.gateway.util.URLHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PlaylistGatewayController implements PlaylistAPI {

    private final ServiceDiscoveryProperties discoveryProperties;
    private final WebClient webClient;

    @Override
    public ResponseEntity<Set<PlaylistDTO>> getAllPlaylists(Integer page, Integer itemsPerPage, Integer userId, String name, Boolean exactMatch) {
        Set<PlaylistDTO> playlists =
                webClient.get().uri(discoveryProperties.getPlaylistServiceLocation() + URLHelper.getFullPath())
                        .retrieve()
                        .bodyToFlux(PlaylistDTO.class)
                        .timeout(Duration.of(1000, ChronoUnit.SECONDS))
                        .collect(Collectors.toSet()).block();

        return ResponseEntity.ok(
                playlists.stream()
                    .map(playlist -> playlist.add(WebMvcLinkBuilder.linkTo(PlaylistGatewayController.class).slash(playlist.getId()).withSelfRel()))
                    .map(playlist -> playlist.add(WebMvcLinkBuilder.linkTo(UserDataGatewayController.class).slash(((PlaylistDTO)playlist).getUserId()).withRel("user")))
                    .map(playlist -> (PlaylistDTO)playlist)
                        .collect(Collectors.toSet())
        );
    }

    @PreAuthorize("#playlistDTO.userId == principal")
    @Override
    public ResponseEntity<Void> addPlaylist(PlaylistDTO playlistDTO) {
        return webClient.post().uri(discoveryProperties.getPlaylistServiceLocation() + URLHelper.getFullPath())
                .bodyValue(playlistDTO)
                .exchangeToMono(clientResponse -> Objects.requireNonNull(clientResponse.toEntity(Void.class)))
                .timeout(Duration.of(1000, ChronoUnit.SECONDS))
                .block();
    }

    @Override
    public ResponseEntity<PlaylistDTO> getPlaylistById(String id) {
        Optional<PlaylistDTO> playlistOptional = Optional.ofNullable(
                webClient.get().uri(discoveryProperties.getPlaylistServiceLocation() + URLHelper.getFullPath())
                .exchangeToMono(clientResponse -> Objects.requireNonNull(clientResponse.toEntity(PlaylistDTO.class)))
                .timeout(Duration.of(1000, ChronoUnit.SECONDS))
                .block().getBody());

        return ResponseEntity.of(
                playlistOptional
                    .map(playlist -> playlist.add(WebMvcLinkBuilder.linkTo(PlaylistGatewayController.class).slash(playlist.getId()).withSelfRel()))
                    .map(playlist -> playlist.add(WebMvcLinkBuilder.linkTo(PlaylistGatewayController.class).withRel("parent")))
                    .map(playlist -> playlist.add(WebMvcLinkBuilder.linkTo(UserDataGatewayController.class).slash(((PlaylistDTO)playlist).getUserId()).withRel("user")))
                    .map(playlist -> (PlaylistDTO)playlist)
        );
    }

    @PreAuthorize("#playlistDTO.userId == authentication.principal")
    @Override
    public ResponseEntity<Void> addOrUpdatePlaylist(String id, PlaylistDTO playlistDTO) {
        return webClient.put().uri(discoveryProperties.getPlaylistServiceLocation() + URLHelper.getFullPath())
                .bodyValue(playlistDTO)
                .exchangeToMono(clientResponse -> Objects.requireNonNull(clientResponse.toEntity(Void.class)))
                .timeout(Duration.of(1000, ChronoUnit.SECONDS))
                .block();
    }

    @Override
    public ResponseEntity<Void> deletePlaylistById(String id) {
        Optional<PlaylistDTO> playlistDTO = Optional.ofNullable(
                webClient.get().uri(discoveryProperties.getPlaylistServiceLocation() + URLHelper.getFullPath())
                        .exchangeToMono(clientResponse -> Objects.requireNonNull(clientResponse.toEntity(PlaylistDTO.class)))
                        .timeout(Duration.of(1000, ChronoUnit.SECONDS))
                        .block().getBody());

        if(!playlistDTO.isPresent())
            return ResponseEntity.notFound().build();

        if(!playlistDTO.get().getUserId().equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
            return ResponseEntity.status(403).build();
        }

        return webClient.delete().uri(discoveryProperties.getPlaylistServiceLocation() + URLHelper.getFullPath())
                .exchangeToMono(clientResponse ->Objects.requireNonNull(clientResponse.toEntity(Void.class)))
                .timeout(Duration.of(1000, ChronoUnit.SECONDS))
                .block();
    }
}

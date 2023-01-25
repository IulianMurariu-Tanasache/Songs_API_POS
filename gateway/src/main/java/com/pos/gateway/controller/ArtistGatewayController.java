package com.pos.gateway.controller;

import com.pos.commons.api.ArtistAPI;
import com.pos.commons.dto.ArtistDTO;
import com.pos.commons.dto.SongDTO;
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
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ArtistGatewayController implements ArtistAPI {

    private final ServiceDiscoveryProperties discoveryProperties;
    private final WebClient webClient;

    @Override
    public ResponseEntity<Set<ArtistDTO>> getAllArtists(Integer page, Integer itemsPerPage, String name, Boolean exactMatch) {
        Set<ArtistDTO> artists =
                webClient.get().uri(discoveryProperties.getArtistServiceLocation() + URLHelper.getFullPath())
                    .retrieve()
                    .bodyToFlux(ArtistDTO.class)
                    .timeout(Duration.of(1000, ChronoUnit.SECONDS))
                    .collect(Collectors.toSet()).block();

        return ResponseEntity.ok(
                artists.stream()
                    .map(artist -> artist.add(WebMvcLinkBuilder.linkTo(ArtistGatewayController.class).slash(artist.getUuid()).withSelfRel()))
                    .map(artist -> artist.add(WebMvcLinkBuilder.linkTo(ArtistGatewayController.class).slash(((ArtistDTO)artist).getUuid()).slash("songs").withRel("songs")))
                    .map(artist -> (ArtistDTO)artist)
                    .collect(Collectors.toSet())
        );
    }

    @Override
    public ResponseEntity<ArtistDTO> getArtistByUUID(String uuid) {
        Optional<ArtistDTO> artistDTO = Optional.ofNullable(
                webClient.get().uri(discoveryProperties.getArtistServiceLocation() + URLHelper.getFullPath())
                .exchangeToMono(clientResponse -> Objects.requireNonNull(clientResponse.toEntity(ArtistDTO.class)))
                .timeout(Duration.of(1000, ChronoUnit.SECONDS))
                .block().getBody()
        );

        return ResponseEntity.of(
                artistDTO
                    .map(artist -> artist.add(WebMvcLinkBuilder.linkTo(ArtistGatewayController.class).slash(artist.getUuid()).withSelfRel()))
                    .map(artist -> artist.add(WebMvcLinkBuilder.linkTo(ArtistGatewayController.class).withRel("parent")))
                    .map(artist -> artist.add(WebMvcLinkBuilder.linkTo(ArtistGatewayController.class).slash(((ArtistDTO)artist).getUuid()).slash("songs").withRel("songs")))
                    .map(artist -> (ArtistDTO)artist)
        );
    }

    @Override
    public ResponseEntity<Set<SongDTO>> getSongsOfArtist(Integer page, Integer itemsPerPage, String uuid) {
        Set<SongDTO> songs =
                webClient.get().uri(discoveryProperties.getArtistServiceLocation() + URLHelper.getFullPath())
                        .retrieve()
                        .bodyToFlux(SongDTO.class)
                        .timeout(Duration.of(1000, ChronoUnit.SECONDS))
                        .collect(Collectors.toSet()).block();

        songs.forEach(song -> song.setArtistSet(song.getArtistSet().stream()
                .map(artistDTO -> artistDTO.add(WebMvcLinkBuilder.linkTo(ArtistGatewayController.class).slash(artistDTO.getUuid()).withSelfRel()))
                .map(artistDTO -> artistDTO.add(WebMvcLinkBuilder.linkTo(ArtistGatewayController.class).withRel("parent")))
                .map(artist -> (ArtistDTO)artist)
                .collect(Collectors.toSet())));

        return ResponseEntity.ok(
                songs.stream()
                        .map(song -> song.add(WebMvcLinkBuilder.linkTo(SongGatewayController.class).slash(song.getId()).withSelfRel()))
                        .map(song -> song.add(WebMvcLinkBuilder.linkTo(SongGatewayController.class).withRel("parent")))
                        .map(song -> song.add(WebMvcLinkBuilder.linkTo(ArtistGatewayController.class).slash(uuid).withRel("artist")))
                        .map(song -> (SongDTO)song)
                        .collect(Collectors.toSet())
        );
    }

    @PreAuthorize("hasRole('ROLE_CMANAGER')")
    @Override
    public ResponseEntity<Void> addArtist(ArtistDTO artistDTO) {
        return webClient.post().uri(discoveryProperties.getArtistServiceLocation() + URLHelper.getFullPath())
                .bodyValue(artistDTO)
                .exchangeToMono(clientResponse -> Objects.requireNonNull(clientResponse.toEntity(Void.class)))
                .timeout(Duration.of(1000, ChronoUnit.SECONDS))
                .block();
    }

    @PreAuthorize("hasRole('ROLE_CMANAGER')")
    @Override
    public ResponseEntity<Void> addOrUpdateArtist(ArtistDTO artistDTO, String uuid) {
        return webClient.put().uri(discoveryProperties.getArtistServiceLocation() + URLHelper.getFullPath())
                .bodyValue(artistDTO)
                .exchangeToMono(clientResponse -> Objects.requireNonNull(clientResponse.toEntity(Void.class)))
                .timeout(Duration.of(1000, ChronoUnit.SECONDS))
                .block();
    }

    @PreAuthorize("hasRole('ROLE_CMANAGER')")
    @Override
    public ResponseEntity<Void> deleteArtistById(String uuid) {
        return webClient.delete().uri(discoveryProperties.getArtistServiceLocation() + URLHelper.getFullPath())
                .exchangeToMono(clientResponse ->Objects.requireNonNull(clientResponse.toEntity(Void.class)))
                .timeout(Duration.of(1000, ChronoUnit.SECONDS))
                .block();
    }
}

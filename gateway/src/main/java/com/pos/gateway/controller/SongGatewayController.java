package com.pos.gateway.controller;

import com.pos.commons.api.SongAPI;
import com.pos.commons.dto.ArtistDTO;
import com.pos.commons.dto.SongCreateDTO;
import com.pos.commons.dto.SongDTO;
import com.pos.commons.enums.MusicType;
import com.pos.commons.enums.SongGenre;
import com.pos.gateway.config.ServiceDiscoveryProperties;
import com.pos.gateway.util.URLHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SongGatewayController implements SongAPI {

    private final ServiceDiscoveryProperties discoveryProperties;
    private final WebClient webClient;
    @Override
    public ResponseEntity<Set<SongDTO>> getAllSongs(Integer page, Integer itemsPerPage, String name, Boolean exactMatch, SongGenre genre, MusicType type, Integer releaseYear) {
        Set<SongDTO> songs =
                webClient.get().uri(discoveryProperties.getSongServiceLocation() + URLHelper.getFullPath())
                    .retrieve()
                    .bodyToFlux(SongDTO.class)
                    .timeout(Duration.of(1000, ChronoUnit.SECONDS))
                    .collect(Collectors.toSet()).block();

        songs = songs.stream()
                    .map(song -> song.add(WebMvcLinkBuilder.linkTo(SongGatewayController.class).slash(song.getId()).withSelfRel()))
                    .map(song -> (SongDTO)song)
                    .collect(Collectors.toSet());

        songs.forEach(song -> song.setArtistSet(song.getArtistSet().stream()
                .map(artistDTO -> artistDTO.add(WebMvcLinkBuilder.linkTo(ArtistGatewayController.class).slash(artistDTO.getUuid()).withSelfRel()))
                .map(artistDTO -> artistDTO.add(WebMvcLinkBuilder.linkTo(ArtistGatewayController.class).withRel("parent")))
                .map(artist -> (ArtistDTO)artist)
                .collect(Collectors.toSet())));

        return ResponseEntity.ok(songs);
    }

    @Override
    public ResponseEntity<SongDTO> getSongById(Integer id) {
        Optional<SongDTO> songDTO = Optional.ofNullable(
            webClient.get().uri(discoveryProperties.getSongServiceLocation() + URLHelper.getFullPath())
                .exchangeToMono(clientResponse -> Objects.requireNonNull(clientResponse.toEntity(SongDTO.class)))
                .timeout(Duration.of(1000, ChronoUnit.SECONDS))
                .block().getBody());

        if(!songDTO.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        songDTO.get().setArtistSet(songDTO.get().getArtistSet().stream()
                .map(artistDTO -> artistDTO.add(WebMvcLinkBuilder.linkTo(ArtistGatewayController.class).slash(artistDTO.getUuid()).withSelfRel()))
                .map(artistDTO -> artistDTO.add(WebMvcLinkBuilder.linkTo(ArtistGatewayController.class).withRel("parent")))
                .map(artist -> (ArtistDTO)artist)
                .collect(Collectors.toSet()));

        return ResponseEntity.of(
                songDTO
                    .map(song -> song.add(WebMvcLinkBuilder.linkTo(SongGatewayController.class).slash(((SongDTO)song).getId()).withSelfRel()))
                    .map(song -> song.add(WebMvcLinkBuilder.linkTo(SongGatewayController.class).withRel("parent")))
                    .map(song -> (SongDTO)song)
        );
    }

    @PreAuthorize("hasAnyRole('ROLE_CMANAGER', 'ROLE_ARTIST')")
    @Override
    public ResponseEntity<Void> addSong(SongCreateDTO song) {
        return webClient.post().uri(discoveryProperties.getSongServiceLocation() + URLHelper.getFullPath())
                .bodyValue(song)
                .exchangeToMono(clientResponse -> Objects.requireNonNull(clientResponse.toEntity(Void.class)))
                .timeout(Duration.of(1000, ChronoUnit.SECONDS))
                .block();
    }

    @PreAuthorize("hasAnyRole('ROLE_CMANAGER', 'ROLE_ARTIST')")
    @Override
    public ResponseEntity<Void> addOrUpdateSong(SongCreateDTO song, Integer id) {
        return webClient.put().uri(discoveryProperties.getSongServiceLocation() + URLHelper.getFullPath())
                .bodyValue(song)
                .exchangeToMono(clientResponse -> Objects.requireNonNull(clientResponse.toEntity(Void.class)))
                .timeout(Duration.of(1000, ChronoUnit.SECONDS))
                .block();
    }

    @PreAuthorize("hasAnyRole('ROLE_CMANAGER', 'ROLE_ARTIST')")
    @Override
    public ResponseEntity<Void> deleteSongById(Integer id) {
        return webClient.delete().uri(discoveryProperties.getSongServiceLocation() + URLHelper.getFullPath())
                .exchangeToMono(clientResponse ->Objects.requireNonNull(clientResponse.toEntity(Void.class)))
                .timeout(Duration.of(1000, ChronoUnit.SECONDS))
                .block();
    }
}

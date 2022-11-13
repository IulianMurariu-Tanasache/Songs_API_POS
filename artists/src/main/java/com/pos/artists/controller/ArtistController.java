package com.pos.artists.controller;

import com.pos.artists.dto.ArtistDTO;
import com.pos.artists.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final ModelMapper mapper;
    private final ArtistService artistService;

    @GetMapping
    public Set<ArtistDTO> getAllArtists() {
        return artistService.findAllArtists().stream()
                .map(artist -> mapper.map(artist, ArtistDTO.class))
                .map(artistDTO -> artistDTO.add(WebMvcLinkBuilder.linkTo(ArtistController.class).slash(artistDTO.getUuid()).withSelfRel()))
                .collect(Collectors.toSet());
    }

    @GetMapping("/{uuid}")
    public ArtistDTO getArtistByUUID(@PathVariable String uuid) {
        return artistService.findByUUID(uuid)
                .map(artist -> mapper.map(artist, ArtistDTO.class))
                .map(artistDTO -> artistDTO.add(WebMvcLinkBuilder.linkTo(ArtistController.class).slash(artistDTO.getUuid()).withSelfRel()))
                .orElseThrow();
    }
}

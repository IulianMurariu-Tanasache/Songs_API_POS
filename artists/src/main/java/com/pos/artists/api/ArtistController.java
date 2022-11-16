package com.pos.artists.api;

import com.pos.artists.service.ArtistService;
import com.pos.commons.dto.ArtistDTO;
import com.pos.commons.entity.Artist;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class ArtistController implements ArtistAPI {

    private final ModelMapper mapper;
    private final ArtistService service;

    @Override
    public ResponseEntity<Set<ArtistDTO>> getAllArtists(Integer page, Integer itemsPerPage) {
        return ResponseEntity.ok(
                service.findAllArtists(page, itemsPerPage).stream()
                    .map(artist -> mapper.map(artist, ArtistDTO.class))
                    //.map(artistDTO -> artistDTO.add(WebMvcLinkBuilder.linkTo(ArtistController.class).slash(artistDTO.getUuid()).withSelfRel()))
                    .collect(Collectors.toSet()));
    }

    @Override
    public ResponseEntity<ArtistDTO> getArtistByUUID(String uuid) {
        try {
            ArtistDTO artistDTO = service.findByUUID(uuid)
                    .map(it -> mapper.map(it, ArtistDTO.class))
                    .orElseThrow(EntityNotFoundException::new);
            return ResponseEntity.ok(artistDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> addArtist(ArtistDTO artistDTO) {
        String uuid = service.saveArtist(mapper.map(artistDTO, Artist.class)).getUuid();
        return ResponseEntity.created(URI.create("/artists/" + uuid)).build();
    }

    @Override
    public ResponseEntity<Void> addOrUpdateArtist(ArtistDTO artistDTO, String uuid) {
        Artist artistToSave = mapper.map(artistDTO, Artist.class);
        if(service.saveOrUpdate(artistToSave, uuid).isPresent())
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.created(URI.create("/artists/" + uuid)).build();
    }

    @Override
    public ResponseEntity<Void> deleteArtist(String uuid) {
        service.deleteArtistByUUID(uuid);
        return ResponseEntity.noContent().build();
    }
}

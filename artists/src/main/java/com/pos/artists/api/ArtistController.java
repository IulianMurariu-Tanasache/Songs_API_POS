package com.pos.artists.api;

import com.pos.artists.service.ArtistService;
import com.pos.commons.api.ArtistAPI;
import com.pos.commons.dto.ArtistDTO;
import com.pos.commons.dto.SongDTO;
import com.pos.commons.entity.Artist;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Set<ArtistDTO>> getAllArtists(Integer page, Integer itemsPerPage, String name, Boolean exactMatch) {
        return ResponseEntity.ok(
                (name == null ?
                    service.findAllArtists(page, itemsPerPage) :
                    service.findArtistsByName(name, page, itemsPerPage, exactMatch))
                        .stream()
                        .map(artist -> mapper.map(artist, ArtistDTO.class))
                        .collect(Collectors.toSet()));

    }

    @Override
    public ResponseEntity<ArtistDTO> getArtistByUUID(String uuid) {
        try {
            ArtistDTO artistDTO = service.findByUUID(uuid)
                    .map(artist -> mapper.map(artist, ArtistDTO.class))
                    .orElseThrow(EntityNotFoundException::new);
            return ResponseEntity.ok(artistDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Set<SongDTO>> getSongsOfArtist(Integer page, Integer itemsPerPage, String uuid) {
        try {
            return ResponseEntity.ok(
                    service.findSongsOfArtist(page, itemsPerPage, uuid).stream()
                            .map(song -> mapper.map(song, SongDTO.class))
                            .collect(Collectors.toSet())
            );
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
    public ResponseEntity<Void> deleteArtistById(String uuid) {
        try {
            service.deleteArtistByUUID(uuid);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}

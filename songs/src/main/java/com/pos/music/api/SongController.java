package com.pos.music.api;

import com.pos.commons.dto.SongDTO;
import com.pos.commons.entity.Song;
import com.pos.music.service.SongService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SongController implements SongAPI {

    private final SongService service;

    private final ModelMapper mapper;

    @Override
    public ResponseEntity<Set<SongDTO>> getAllSongs(Integer page, Integer itemsPerPage) {
        return ResponseEntity.ok(
                service.findAllSongs(page, itemsPerPage).stream()
                    .map(song -> mapper.map(song, SongDTO.class))
                    .collect(Collectors.toSet()));
    }

    @Override
    public ResponseEntity<SongDTO> getSongById(Integer id) {
        try {
            Song song = service.findSongById(id).orElseThrow(EntityNotFoundException::new);
            SongDTO songDTO = mapper.map(song, SongDTO.class);
            return ResponseEntity.ok(songDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> addSong(SongDTO song) {
        Song songToSave = mapper.map(song, Song.class);
        int id = service.saveSong(songToSave).getId();
        return ResponseEntity.created(URI.create("/songs/" + id)).build();
    }

    @Override
    public ResponseEntity<Void> addOrModifySong(SongDTO song, Integer id) {
        Song songToSave = mapper.map(song, Song.class);
        if(service.saveOrUpdate(songToSave, id).isPresent())
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.created(URI.create("/songs/" + id)).build();
    }

    @Override
    public ResponseEntity<Void> deleteSong(Integer id) {
        service.deleteSongById(id);
        return ResponseEntity.noContent().build();
    }
}

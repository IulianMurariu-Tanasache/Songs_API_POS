package com.pos.music.api;

import com.pos.music.dto.SongDTO;
import com.pos.music.entity.Song;
import com.pos.music.service.SongService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SongController implements SongAPI {

    private final SongService service;

    private final ModelMapper mapper;

    @Override
    public ResponseEntity<Set<SongDTO>> getAllSongs() {
        return ResponseEntity.ok(
                service.findAllSong().stream()
                    .map(song -> mapper.map(song, SongDTO.class))
                    .collect(Collectors.toSet()));
    }

    @Override
    public ResponseEntity<SongDTO> getSongById(Integer id) {
        return service.findSongById(id);
    }

    @Override
    public ResponseEntity<Void> addSong(SongDTO song) {
        Song songToSave = mapper.map(song, Song.class);
        service.save(songToSave);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> addOrModifySong(SongDTO song, Integer id) {
        Song songToSave = mapper.map(song, Song.class);
        songToSave.setId(id);
        service.save(songToSave);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteSong(Integer id) {
        service.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}

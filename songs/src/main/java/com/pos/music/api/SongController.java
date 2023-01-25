package com.pos.music.api;

import com.pos.commons.api.SongAPI;
import com.pos.commons.dto.SongCreateDTO;
import com.pos.commons.dto.SongDTO;
import com.pos.commons.entity.Song;
import com.pos.commons.enums.MusicType;
import com.pos.commons.enums.SongGenre;
import com.pos.music.service.SongService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Set<SongDTO>> getAllSongs
            (Integer page,
            Integer itemsPerPage,
            String name,
            Boolean exactMatch,
            SongGenre genre,
            MusicType type,
            Integer releaseYear) {
        Set<SongDTO> songs = service.findAllSongs(page, itemsPerPage, name, exactMatch, genre, type, releaseYear).stream()
                .map(song -> mapper.map(song, SongDTO.class))
                .collect(Collectors.toSet());

        return ResponseEntity.ok(songs);
    }

    @Override
    public ResponseEntity<SongDTO> getSongById(Integer id) {
        try {
            Song foundSong = service.findSongById(id).orElseThrow(EntityNotFoundException::new);
            SongDTO songDTO = mapper.map(foundSong, SongDTO.class);

            return ResponseEntity.ok(songDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> addSong(SongCreateDTO song) {
        try {
            Song songToSave = mapper.map(song, Song.class);
            int id = service.saveSong(songToSave).getId();
            return ResponseEntity.created(URI.create("/api/songcollection/songs" + id)).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> addOrUpdateSong(SongCreateDTO song, Integer id) {
        try {
            Song songToSave = mapper.map(song, Song.class);
            if (service.saveOrUpdate(songToSave, id).isPresent())
                return ResponseEntity.noContent().build();
            else
                return ResponseEntity.created(URI.create("/api/songcollection/songs" + id)).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteSongById(Integer id) {
        try{
            service.deleteSongById(id);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}

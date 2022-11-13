package com.pos.music.api;

import com.pos.music.dto.SongDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping("/api/songcollection/songs")
public interface SongAPI {

    @GetMapping(value = "/", produces = {"application/json"})
    ResponseEntity<Set<SongDTO>> getAllSongs();

    @GetMapping("/{id}")
    ResponseEntity<SongDTO> getSongById(@PathVariable Integer id);

    @PostMapping
    ResponseEntity<Void> addSong(@RequestBody SongDTO song);

    @PutMapping("/{id}")
    ResponseEntity<Void> addOrModifySong(@RequestBody SongDTO song, @PathVariable Integer id);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteSong(@PathVariable Integer id);
}

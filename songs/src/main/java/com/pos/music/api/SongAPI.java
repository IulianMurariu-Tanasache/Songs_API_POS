package com.pos.music.api;

import com.pos.commons.dto.SongDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping("/api/songcollection/songs")
public interface SongAPI {
    // TODO: more work on the paging uri -> copy from artists and add more
    @GetMapping
    ResponseEntity<Set<SongDTO>> getAllSongs(@RequestParam Integer page, @RequestParam("items_per_page") Integer itemsPerPage);

    @GetMapping("/{id}")
    ResponseEntity<SongDTO> getSongById(@PathVariable Integer id);

    @PostMapping
    ResponseEntity<Void> addSong(@RequestBody SongDTO song);

    @PutMapping("/{id}")
    ResponseEntity<Void> addOrModifySong(@RequestBody SongDTO song, @PathVariable Integer id);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteSong(@PathVariable Integer id);
}

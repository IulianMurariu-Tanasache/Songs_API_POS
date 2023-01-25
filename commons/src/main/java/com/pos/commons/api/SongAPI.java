package com.pos.commons.api;

import com.pos.commons.dto.SongCreateDTO;
import com.pos.commons.dto.SongDTO;
import com.pos.commons.enums.MusicType;
import com.pos.commons.enums.SongGenre;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping("/api/songcollection/songs")
public interface SongAPI {
    @GetMapping
    ResponseEntity<Set<SongDTO>> getAllSongs
            (@RequestParam(required = false) Integer page,
             @RequestParam(value = "items_per_page", defaultValue = "10") Integer itemsPerPage,
             @RequestParam(required = false) String name,
             @RequestParam(value = "exact_match", defaultValue = "false") Boolean exactMatch,
             @RequestParam(required = false) SongGenre genre,
             @RequestParam(required = false) MusicType type,
             @RequestParam(required = false, value = "release_year") Integer releaseYear);

    @GetMapping("/{id}")
    ResponseEntity<SongDTO> getSongById(@PathVariable Integer id);

    @PostMapping
    ResponseEntity<Void> addSong(@RequestBody SongCreateDTO song);

    @PutMapping("/{id}")
    ResponseEntity<Void> addOrUpdateSong(@RequestBody SongCreateDTO song, @PathVariable Integer id);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteSongById(@PathVariable Integer id);
}

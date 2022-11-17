package com.pos.artists.api;

import com.pos.commons.dto.ArtistDTO;
import com.pos.commons.dto.SongDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping("/api/songcollection/artists")
public interface ArtistAPI {
    @GetMapping
    ResponseEntity<Set<ArtistDTO>> getAllArtists
            (@RequestParam(required = false) Integer page,
            @RequestParam(value = "items_per_page", defaultValue = "10") Integer itemsPerPage,
            @RequestParam(required = false) String name,
            @RequestParam(value = "exact_match", defaultValue = "true") Boolean exactMatch);

    @GetMapping("/{uuid}")
    ResponseEntity<ArtistDTO> getArtistByUUID(@PathVariable String uuid);

    @GetMapping("/{uuid}/songs")
    ResponseEntity<Set<SongDTO>> getSongsOfArtist(@PathVariable String uuid);

    @PostMapping
    ResponseEntity<Void> addArtist(@RequestBody ArtistDTO artistDTO);

    @PutMapping("/{uuid}")
    ResponseEntity<Void> addOrUpdateArtist(@RequestBody ArtistDTO artistDTO, @PathVariable String uuid);

    @DeleteMapping("/{uuid}")
    ResponseEntity<Void> deleteArtist(@PathVariable String uuid);
}

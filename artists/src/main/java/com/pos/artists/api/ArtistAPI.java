package com.pos.artists.api;

import com.pos.commons.dto.ArtistDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping("/api/songcollection/artists/")
public interface ArtistAPI {
    @GetMapping
    ResponseEntity<Set<ArtistDTO>> getAllArtists(@RequestParam Integer page, @RequestParam("items_per_page") Integer itemsPerPage);

    @GetMapping("/{uuid}")
    ResponseEntity<ArtistDTO> getArtistByUUID(@PathVariable String uuid);

    @PostMapping
    ResponseEntity<Void> addArtist(@RequestBody ArtistDTO artistDTO);

    @PutMapping("/{uuid}")
    ResponseEntity<Void> addOrUpdateArtist(@RequestBody ArtistDTO artistDTO, @PathVariable String uuid);

    @DeleteMapping("/{uuid}")
    ResponseEntity<Void> deleteArtist(@PathVariable String uuid);
}

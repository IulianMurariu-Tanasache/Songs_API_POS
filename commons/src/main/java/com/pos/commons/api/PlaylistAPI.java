package com.pos.commons.api;

import com.pos.commons.dto.PlaylistDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping("/api/playlists")
public interface PlaylistAPI {

    @GetMapping
    ResponseEntity<Set<PlaylistDTO>> getAllPlaylists(
            @RequestParam(required = false) Integer page,
            @RequestParam(value = "items_per_page", defaultValue = "10") Integer itemsPerPage,
            @RequestParam(required = false, value = "user_id") Integer userId,
            @RequestParam(required = false) String name,
            @RequestParam(value = "exact_match", defaultValue = "false") Boolean exactMatch

    );

    @PostMapping
    ResponseEntity<Void> addPlaylist(@RequestBody PlaylistDTO playlistDTO);

    @GetMapping("/{id}")
    ResponseEntity<PlaylistDTO> getPlaylistById(@PathVariable String id);

    @PutMapping("/{id}")
    ResponseEntity<Void> addOrUpdatePlaylist(@PathVariable String id, @RequestBody PlaylistDTO newPlaylist);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletePlaylistById(@PathVariable String id);
}

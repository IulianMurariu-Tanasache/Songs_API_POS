package com.pos.playlist.api;

import com.pos.commons.api.PlaylistAPI;
import com.pos.commons.dto.PlaylistDTO;
import com.pos.playlist.entity.Playlist;
import com.pos.playlist.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PlaylistController implements PlaylistAPI {

    private final ModelMapper mapper;
    private final PlaylistService playlistService;

    @Override
    public ResponseEntity<Set<PlaylistDTO>> getAllPlaylists(Integer page, Integer itemsPerPage, Integer userId, String name, Boolean exactMatch) {
        return ResponseEntity.ok(
                playlistService.getAllPlaylists(page, itemsPerPage, userId, name, exactMatch).stream()
                        .map(playlist -> mapper.map(playlist, PlaylistDTO.class))
                        .collect(Collectors.toSet()));
    }

    @Override
    public ResponseEntity<Void> addPlaylist(PlaylistDTO playlistDTO) {
        Playlist savedPlaylist = playlistService.savePlaylist(mapper.map(playlistDTO, Playlist.class));
        return ResponseEntity.created(URI.create("/api/playlist/" + savedPlaylist.getId())).build();
    }

    @Override
    public ResponseEntity<PlaylistDTO> getPlaylistById(String id) {
         return ResponseEntity.of(
                 playlistService.getPlaylist(id)
                    .map(playlist -> mapper.map(playlist, PlaylistDTO.class))
         );
    }

    @Override
    public ResponseEntity<Void> addOrUpdatePlaylist(String id, PlaylistDTO newPlaylist) {
        Optional<Playlist> playlistOptional = playlistService.updatePlaylist(id, mapper.map(newPlaylist, Playlist.class));
        if(playlistOptional.isPresent())
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.created(URI.create("/api/playlist/" + id)).build();
    }

    @Override
    public ResponseEntity<Void> deletePlaylistById(String id) {
        try {
            playlistService.deletePlaylistById(id);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}

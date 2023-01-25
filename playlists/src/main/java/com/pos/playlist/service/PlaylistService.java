package com.pos.playlist.service;

import com.pos.playlist.entity.Playlist;

import java.util.List;
import java.util.Optional;

public interface PlaylistService {
    List<Playlist> getAllPlaylists(Integer page, Integer itemsPerPage, Integer userId, String name, Boolean exactMatch);
    Playlist savePlaylist(Playlist playlist);

    Optional<Playlist> getPlaylist(String id);

    void deletePlaylistById(String id);

    Optional<Playlist> updatePlaylist(String id, Playlist playlist);
}

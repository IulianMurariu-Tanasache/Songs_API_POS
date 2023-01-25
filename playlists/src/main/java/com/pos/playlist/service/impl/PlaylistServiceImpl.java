package com.pos.playlist.service.impl;

import com.pos.playlist.entity.Playlist;
import com.pos.playlist.repository.PlaylistRepository;
import com.pos.playlist.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.pos.commons.util.ServiceUtility.makePageRequest;

@Service
@RequiredArgsConstructor
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository repository;

    private List<Playlist> findPlaylistsByName(PageRequest pageRequest, String name, Boolean exactMatch) {
        return (exactMatch ?
                repository.findByName(pageRequest, name) :
                repository.findByNameLike(pageRequest, name));
    }

    @Override
    public List<Playlist> getAllPlaylists(Integer page, Integer itemsPerPage, Integer userId, String name, Boolean exactMatch) {
        List<Playlist> playlists = name == null ?
                repository.findAll(makePageRequest(page, itemsPerPage)).toList():
                this.findPlaylistsByName(makePageRequest(page, itemsPerPage), name, exactMatch);

        if(userId != null)
            playlists = playlists.stream().filter(playlist -> Objects.equals(playlist.getUserId(), userId)).collect(Collectors.toList());

        return playlists;
    }

    @Override
    public Playlist savePlaylist(Playlist playlist) {
        if(playlist.getSongs() == null)
            playlist.setSongs(new HashSet<>());
        repository.save(playlist);
        return playlist;
    }

    @Override
    public Optional<Playlist> getPlaylist(String id) {
        return repository.findById(id);
    }

    @Override
    public void deletePlaylistById(String id) {
        if(!repository.existsById(id))
            throw new EntityNotFoundException();
        repository.deleteById(id);
    }

    @Override
    public Optional<Playlist> updatePlaylist(String id, Playlist playlist) {
        playlist.setId(id);
        Optional<Playlist> existing = repository.findById(id);
        repository.save(playlist);
        return existing;
    }
}

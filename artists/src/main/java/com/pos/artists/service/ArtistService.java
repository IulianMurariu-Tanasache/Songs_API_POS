package com.pos.artists.service;

import com.pos.artists.entity.Artist;

import java.util.Optional;
import java.util.Set;

public interface ArtistService {
    Optional<Artist> findArtistByName(String name);
    Set<Artist> findActiveArtists(boolean active);
    Set<Artist> findAllArtists();
    Optional<Artist> findByUUID(String uuid);
    void deleteArtistByName(String name);
    void saveArtist(Artist artist);
}
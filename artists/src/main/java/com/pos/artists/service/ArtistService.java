package com.pos.artists.service;

import com.pos.commons.entity.Artist;

import java.util.Optional;
import java.util.Set;

public interface ArtistService {
    Set<Artist> findArtistsByName(String name, Integer page, Integer itemsPerPage, Boolean exactMatch);
    Set<Artist> findAllArtists(Integer page, Integer itemsPerPage);
    Optional<Artist> findByUUID(String uuid);
    void deleteArtistByName(String name);
    Artist saveArtist(Artist artist);
    Optional<Artist> saveOrUpdate(Artist artist, String uuid);
    void deleteArtistByUUID(String uuid);
}
package com.pos.artists.service.impl;

import com.pos.artists.entity.Artist;
import com.pos.artists.repository.ArtistRepository;
import com.pos.artists.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;

    @Override
    public Optional<Artist> findArtistByName(String name) {
        return artistRepository.findByName(name);
    }

    @Override
    public Set<Artist> findActiveArtists(boolean active) {
        return artistRepository.findActiveArtists(active);
    }

    @Override
    public Set<Artist> findAllArtists() {
        Set<Artist> set = new HashSet<>();
        artistRepository.findAll().forEach(set::add);
        return set;
    }

    @Override
    public Optional<Artist> findByUUID(String uuid) {
        return artistRepository.findById(uuid);
    }

    @Override
    public void deleteArtistByName(String name) {
        artistRepository.deleteByName(name);
    }

    @Override
    public void saveArtist(Artist artist) {
        artistRepository.save(artist);
    }
}

package com.pos.artists.service.impl;

import com.pos.artists.repository.ArtistRepository;
import com.pos.artists.service.ArtistService;
import com.pos.commons.entity.Artist;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
    public Set<Artist> findAllArtists(Integer page, Integer itemsPerPage) {
        Set<Artist> set = new HashSet<>();
        artistRepository.findAll(PageRequest.of(page, itemsPerPage, Sort.by("name").ascending()))
                .forEach(set::add);
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
    public Artist saveArtist(Artist artist) {
        artist.setUuid(UUID.randomUUID().toString());
        return artistRepository.save(artist);
    }

    @Override
    public Optional<Artist> saveOrUpdate(Artist artist, String uuid) {
        artist.setUuid(uuid);
        if(artistRepository.existsById(uuid))
            return Optional.of(artistRepository.save(artist));
        else {
            artistRepository.save(artist);
            return Optional.empty();
        }
    }

    @Override
    public void deleteArtistByUUID(String uuid) {
        artistRepository.deleteById(uuid);
    }
}

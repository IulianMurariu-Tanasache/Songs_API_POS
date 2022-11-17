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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {
    private final ArtistRepository artistRepository;

    @Override
    public Set<Artist> findArtistsByName(String name, Integer page, Integer itemsPerPage, Boolean exactMatch) {
        PageRequest pageRequest = page == null ?
                PageRequest.of(0, Integer.MAX_VALUE, Sort.by("name").ascending()) :
                PageRequest.of(page, itemsPerPage, Sort.by("name").ascending());

        return (exactMatch ?
                    artistRepository.findByName(pageRequest, name) :
                    artistRepository.findByNameLike(pageRequest, name))
                        .stream()
                        .collect(Collectors.toSet());
    }

    @Override
    public Set<Artist> findAllArtists(Integer page, Integer itemsPerPage) {
        PageRequest pageRequest = page == null ?
                PageRequest.of(0, Integer.MAX_VALUE, Sort.by("name").ascending()) :
                PageRequest.of(page, itemsPerPage, Sort.by("name").ascending());

        return artistRepository.findAll(pageRequest)
                .stream()
                .collect(Collectors.toSet());
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

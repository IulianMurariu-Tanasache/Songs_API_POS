package com.pos.artists.service.impl;

import com.pos.artists.repository.ArtistRepository;
import com.pos.artists.repository.SongsOfArtistRepository;
import com.pos.artists.service.ArtistService;
import com.pos.commons.entity.Artist;
import com.pos.commons.entity.Song;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.pos.commons.util.ServiceUtility.makePageRequest;

@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {
    private final ArtistRepository artistRepository;
    private final SongsOfArtistRepository songsOfArtistRepository;

    @Override
    public Set<Artist> findArtistsByName(String name, Integer page, Integer itemsPerPage, Boolean exactMatch) {
        return (exactMatch ?
                    artistRepository.findByName(makePageRequest(page, itemsPerPage), name) :
                    artistRepository.findByNameLike(makePageRequest(page, itemsPerPage), name))
                        .stream()
                        .collect(Collectors.toSet());
    }

    @Override
    public Set<Artist> findAllArtists(Integer page, Integer itemsPerPage) {
        return artistRepository.findAll(makePageRequest(page, itemsPerPage))
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
        Optional<Artist> existing = artistRepository.findById(uuid);
        artistRepository.save(artist);
        return existing;
    }

    @Override
    public void deleteArtistByUUID(String uuid) {
        artistRepository.deleteById(uuid);
    }

    @Override
    public Set<Song> findSongsOfArtist(Integer page, Integer itemsPerPage, String uuid) {
        if(!artistRepository.existsById(uuid))
            throw new EntityNotFoundException();

        return songsOfArtistRepository.findSongsOfArtist(makePageRequest(page, itemsPerPage), uuid)
                .stream()
                .collect(Collectors.toSet());
    }
}

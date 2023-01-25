package com.pos.music.service.impl;

import com.pos.commons.entity.Artist;
import com.pos.commons.entity.Song;
import com.pos.commons.enums.MusicType;
import com.pos.commons.enums.SongGenre;
import com.pos.music.repository.ArtistRepository;
import com.pos.music.repository.SongRepository;
import com.pos.music.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.pos.commons.util.ServiceUtility.makePageRequest;

@RequiredArgsConstructor
@Service
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;
    private final ArtistRepository artistRepository;

    @Override
    public Optional<Song> findSongById(Integer id) {
        return songRepository.findById(id);
    }

    private Set<Song> findSongsByName(PageRequest pageRequest, String name, Boolean exactMatch) {
        return (exactMatch ?
                songRepository.findByName(pageRequest, name) :
                songRepository.findByNameLike(pageRequest, name));
    }

//    @Override
//    public Set<Song> findSongByArtist(String artist) {
//        return songRepository.findSongsByArtist(artist);
//    }

    @Override
    public Set<Song> findAllSongs(Integer page,
                                  Integer itemsPerPage,
                                  String name,
                                  Boolean exactMatch,
                                  SongGenre genre,
                                  MusicType type,
                                  Integer releaseYear) {

        Set<Song> songs = name == null ?
                songRepository.findAll(makePageRequest(page, itemsPerPage)).toSet() :
                this.findSongsByName(makePageRequest(page, itemsPerPage), name, exactMatch);

        if(genre != null)
            songs = songs.stream().filter(song -> song.getGenre().equals(genre)).collect(Collectors.toSet());
        if(type != null)
            songs = songs.stream().filter(song -> song.getType().equals(type)).collect(Collectors.toSet());
        if(releaseYear != null)
            songs = songs.stream().filter(song -> song.getReleaseYear().equals(releaseYear)).collect(Collectors.toSet());

        return songs;
    }

    @Override
    public void deleteSongById(Integer id) {
        songRepository.deleteById(id);
    }

    @Override
    public Song saveSong(Song song) throws EntityNotFoundException {
        Set<Artist> artistDBSet = new HashSet<>();
        for(Artist artistReceived : song.getArtistSet()) {
            System.out.println(artistReceived.getUuid());
            Optional<Artist> artist = artistRepository.findArtistByUUID(artistReceived.getUuid());
            artistDBSet.add(artist.orElseThrow(() -> new EntityNotFoundException("Artist not found!")));
        }
        song.setArtistSet(artistDBSet);
        return songRepository.save(song);
    }

    @Override
    public Optional<Song> saveOrUpdate(Song song, Integer id) throws EntityNotFoundException {
        song.setId(id);
        Optional<Song> existing = songRepository.findById(id);
        songRepository.save(song);
        return existing;
    }
}

package com.pos.music.service.impl;

import com.pos.commons.entity.Song;
import com.pos.commons.enums.MusicType;
import com.pos.commons.enums.SongGenre;
import com.pos.music.repository.SongRepository;
import com.pos.music.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;

    @Override
    public Optional<Song> findSongById(Integer id) {
        return songRepository.findById(id);
    }

    @Override
    public Set<Song> findSongByGenre(SongGenre genre) {
        return songRepository.findSongByGenre(genre);
    }

    @Override
    public Set<Song> findSongByType(MusicType type) {
        return songRepository.findSongByType(type);
    }

    @Override
    public Set<Song> findSongByArtist(String artist) {
        return songRepository.findSongByArtist(artist);
    }

    @Override
    public Set<Song> findSongByReleaseYear(Integer year) {
        return songRepository.findSongByReleaseYear(year);
    }

    @Override
    public Set<Song> findAllSongs(Integer page, Integer itemsPerPage) {
        Set<Song> set = new HashSet<>();
        songRepository.findAll(PageRequest.of(page,itemsPerPage, Sort.by("name").ascending()))
                .forEach(set::add);
        return set;
    }

    @Override
    public void deleteSongByName(String name) {
        songRepository.deleteSongByName(name);
    }

    @Override
    public void deleteSongById(Integer id) {
        songRepository.deleteById(id);
    }

    @Override
    public Song saveSong(Song song) {
        return songRepository.save(song);
    }

    @Override
    public Optional<Song> saveOrUpdate(Song song, Integer id) {
        song.setId(id);
        if(songRepository.existsById(id))
            return Optional.of(songRepository.save(song));
        else {
            songRepository.save(song);
            return Optional.empty();
        }
    }
}

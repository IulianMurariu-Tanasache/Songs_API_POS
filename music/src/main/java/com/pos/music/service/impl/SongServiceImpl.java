package com.pos.music.service.impl;

import com.pos.music.entity.Song;
import com.pos.music.enums.MusicType;
import com.pos.music.enums.SongGenre;
import com.pos.music.repository.SongRepository;
import com.pos.music.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;

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
    public Set<Song> findAllSong() {
        Set<Song> set = new HashSet<>();
        songRepository.findAll().forEach(set::add);
        return set;
    }

    @Override
    public void deleteSongByName(String name) {
        songRepository.deleteSongByName(name);
    }

    @Override
    public void saveSong(Song song) {
        songRepository.save(song);
    }
}

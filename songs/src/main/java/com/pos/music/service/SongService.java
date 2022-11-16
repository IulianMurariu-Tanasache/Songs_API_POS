package com.pos.music.service;

import com.pos.commons.entity.Song;
import com.pos.commons.enums.MusicType;
import com.pos.commons.enums.SongGenre;

import java.util.Optional;
import java.util.Set;

public interface SongService {
    Optional<Song> findSongById(Integer id);
    Set<Song> findSongByGenre(SongGenre genre);
    Set<Song> findSongByType(MusicType type);
    Set<Song> findSongByArtist(String artist);
    Set<Song> findSongByReleaseYear(Integer year);
    Set<Song> findAllSongs(Integer page, Integer itemsPerPage);
    void deleteSongByName(String name);
    void deleteSongById(Integer id);
    Song saveSong(Song song);
    Optional<Song> saveOrUpdate(Song song, Integer id);
}

package com.pos.music.service;

import com.pos.music.entity.Song;
import com.pos.music.enums.MusicType;
import com.pos.music.enums.SongGenre;

import java.util.Set;

public interface SongService {
    Optional<Song> findSongById(Integer id);
    Set<Song> findSongByGenre(SongGenre genre);
    Set<Song> findSongByType(MusicType type);
    Set<Song> findSongByArtist(String artist);
    Set<Song> findSongByReleaseYear(Integer year);
    Set<Song> findAllSong();
    void deleteSongByName(String name);
    void saveSong(Song song);
}

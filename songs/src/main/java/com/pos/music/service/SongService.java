package com.pos.music.service;

import com.pos.commons.entity.Song;
import com.pos.commons.enums.MusicType;
import com.pos.commons.enums.SongGenre;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.Set;

public interface SongService {
    Optional<Song> findSongById(Integer id);

    Set<Song> findAllSongs(Integer page,
                           Integer itemsPerPage,
                           String name,
                           Boolean exactMatch,
                           SongGenre genre,
                           MusicType type,
                           Integer releaseYear);

    void deleteSongById(Integer id);

    Song saveSong(Song song) throws EntityNotFoundException;

    Optional<Song> saveOrUpdate(Song song, Integer id) throws EntityNotFoundException;
}

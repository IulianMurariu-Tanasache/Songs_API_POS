package com.pos.music.repository;

import com.pos.commons.entity.Song;
import com.pos.commons.enums.MusicType;
import com.pos.commons.enums.SongGenre;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SongRepository extends PagingAndSortingRepository<Song, Integer> {

    Set<Song> findSongByGenre(SongGenre genre);
    Set<Song> findSongByType(MusicType type);
    @Query(nativeQuery = true, value =
            "SELECT m.* " +
            "FROM music as m, music_artists as m_a, artists as a " +
            "WHERE m.id = m_a.id_music AND " +
            "m_a.id = a.id AND " +
            "a.name = :artist"
    )
    Set<Song> findSongsByArtist(String artist);
    Set<Song> findSongByReleaseYear(Integer year);
    void deleteSongByName(String name);
}

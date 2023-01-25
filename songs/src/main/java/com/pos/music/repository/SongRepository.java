package com.pos.music.repository;

import com.pos.commons.entity.Song;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Set;

@Repository
@Transactional
public interface SongRepository extends PagingAndSortingRepository<Song, Integer> {

    @Query(nativeQuery = true, value =
            "SELECT m.* " +
            "FROM music as m, music_artists as m_a, artists as a " +
            "WHERE m.id = m_a.id_music AND " +
            "m_a.id = a.id AND " +
            "a.name = :artist"
    )
    Set<Song> findSongsByArtist(String artist);
    void deleteSongByName(String name);

    Set<Song> findByName(PageRequest pageRequest, String name);

    @Query(value = "SELECT * FROM music WHERE name LIKE %:name%", nativeQuery = true)
    Set<Song> findByNameLike(PageRequest pageRequest, String name);
}

package com.pos.artists.repository;

import com.pos.commons.entity.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SongsOfArtistRepository extends PagingAndSortingRepository<Song, Integer> {
    @Query(value = "SELECT music.id,music.genre,music.name,music.release_year,music.type FROM music, music_artists WHERE music_artists.id_music = music.id AND music_artists.id_artist = :uuid",
            nativeQuery = true)
    Page<Song> findSongsOfArtist(PageRequest pageRequest, String uuid);
}

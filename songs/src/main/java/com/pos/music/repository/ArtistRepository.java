package com.pos.music.repository;

import com.pos.commons.entity.Artist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistRepository extends PagingAndSortingRepository<Artist, String> {
    @Query(value = "SELECT * FROM artists WHERE uuid = ?1", nativeQuery = true)
    Optional<Artist> findArtistByUUID(String uuid);
}

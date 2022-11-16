package com.pos.artists.repository;

import com.pos.commons.entity.Artist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ArtistRepository extends PagingAndSortingRepository<Artist, String> {
    Optional<Artist> findByName(String name);
    @Query("select a from Artist a where a.active = :active")
    Set<Artist> findActiveArtists(boolean active);
    void deleteByName(String name);
}

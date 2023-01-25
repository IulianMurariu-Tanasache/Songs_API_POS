package com.pos.artists.repository;

import com.pos.commons.entity.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends PagingAndSortingRepository<Artist, String> {
    Page<Artist> findByName(Pageable pageable, String name);
    void deleteByName(String name);

    @Query(nativeQuery = true,
        value = "SELECT * FROM ARTISTS WHERE NAME LIKE %:name%")
    Page<Artist> findByNameLike(PageRequest pageRequest, String name);
}

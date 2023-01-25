package com.pos.playlist.repository;

import com.pos.playlist.entity.Playlist;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends PagingAndSortingRepository<Playlist, String> {
    List<Playlist> findByName(PageRequest pageRequest, String name);

    @Query("{'name': {$regex: .*:name.*}}")
    List<Playlist> findByNameLike(PageRequest pageRequest, String name);
}

package com.pos.user_data.repository;

import com.pos.user_data.entity.UserData;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDataRepository extends PagingAndSortingRepository<UserData, Integer> {
    Optional<UserData> findByUserId(Integer id);
    void deleteByUserId(Integer id);

    @Query("{'username': {$regex: '.*?0.*'}}")
    List<UserData> findByUsernameLike(String username, PageRequest pageRequest);

    @Query("{'firstName': {$regex: '.*?0.*'}}")
    List<UserData> findByFirstNameLike(String firstName, PageRequest pageRequest);

    @Query("{'lastName': {$regex: '.*?0.*'}}")
    List<UserData> findByLastNameLike(String lastName, PageRequest pageRequest);

    @Query("{'email': {$regex: '.*?0.*'}}")
    List<UserData> findByEmailLike(String email, PageRequest pageRequest);
}

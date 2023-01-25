package com.pos.idm.repository;

import com.pos.idm.entity.TokenEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JWTRepository extends CrudRepository<TokenEntity, String> {
}

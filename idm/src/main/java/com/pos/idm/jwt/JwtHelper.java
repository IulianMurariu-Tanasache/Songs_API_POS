package com.pos.idm.jwt;

import com.pos.idm.entity.TokenEntity;
import com.pos.idm.exception.InvalidJWTException;
import com.pos.idm.repository.JWTRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtHelper {

    @Value("${jwt.secret-key}")
    private String key;

    @Value("${jwt.duration}")
    private Integer jwtDuration;

    private final JWTRepository repository;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(key.getBytes());
    }

    public String generateJwt(String username, Set<String> authorities) {
        String uuid = UUID.randomUUID().toString();
        String token =  Jwts.builder()
                .setSubject(username)
                .claim("authorities", authorities)
                .setIssuedAt(new Date())
                .setId(uuid)
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtDuration)))
                .signWith(getSecretKey())
                .compact();

        repository.save(new TokenEntity(uuid, token));

        return token;
    }

    // return authorities granted by the given jwt
    public List<String> verifyToken(String jwt) throws InvalidJWTException {
        try {
            Claims body = getJWTBody(jwt);
            String username = body.getSubject();
            String id = body.getId();

            if(!repository.existsById(id))
                throw new InvalidJWTException();

            List<String> authorities = (List<String>) body.get("authorities");

            System.out.println(authorities);
            return authorities;
        } catch (JwtException e) {
            invalidateToken(jwt);
            throw new InvalidJWTException();
        }
    }

    public Claims getJWTBody(String jwt) throws InvalidJWTException{
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(getSecretKey())
                    .parseClaimsJws(jwt);

            return claimsJws.getBody();
        } catch (JwtException e) {
            throw new InvalidJWTException();
        }
    }


    public void invalidateToken(String jwt) throws InvalidJWTException {
        try {
            Claims body = getJWTBody(jwt);
            String id = body.getId();

            if(repository.existsById(id)) {
                repository.deleteById(id);
            }
        } catch (JwtException e) {
            throw new InvalidJWTException();
        }
    }
}

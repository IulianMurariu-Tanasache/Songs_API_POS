package com.pos.idm.jwt;

import com.pos.idm.exception.InvalidJWTException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class JwtHelper {

    @Value("${jwt.secret-key}")
    private String key;

    @Value("${jwt.duration}")
    private Integer jwtDuration;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(key.getBytes());
    }

    public String generateJwt(String username, Set<String> authorities) {
        String token = Jwts.builder()
                .setSubject(username)
                .claim("authorities", authorities)
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtDuration)))
                .signWith(getSecretKey())
                .compact();

        return token;
    }

    // return authorities granted by the given jwt
    public List<Map<String, String>> verifyToken(String jwt) throws InvalidJWTException {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(getSecretKey())
                    .parseClaimsJws(jwt);

            Claims body = claimsJws.getBody();
            String username = body.getSubject();
            List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");

            System.out.println(authorities);
            return authorities;
        } catch (JwtException e) {
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

}

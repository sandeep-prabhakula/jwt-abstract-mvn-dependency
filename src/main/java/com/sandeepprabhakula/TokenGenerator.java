package com.sandeepprabhakula;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public class TokenGenerator {
    private final Logger log = LoggerFactory.getLogger(TokenGenerator.class);

    public String generateToken(String SECRET, String userName, Map<String, Object> claims, long expirationDuration) {
        try {
            return createToken(SECRET, claims, userName, expirationDuration);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private String createToken(String SECRET, Map<String, Object> claims, String userName, long expiration) {
        try {
            expiration = expiration != 0 ? expiration : 1000 * 60 * 30;
            log.info(String.valueOf(expiration));
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userName)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(getSignKey(SECRET), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }

    }

    private Key getSignKey(String SECRET) {
        try {
            byte[] keyBytes = Decoders.BASE64URL.decode(SECRET);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }


    public Date extractExpiration(String SECRET, String token) {
        try {
            return extractClaim(SECRET, token, Claims::getExpiration);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private <T> T extractClaim(String SECRET, String token, Function<Claims, T> claimsResolver) {
        try {

            final Claims claims = extractAllClaims(SECRET, token);
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Claims extractAllClaims(String SECRET, String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignKey(SECRET))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private Boolean isTokenExpired(String SECRET, String token) {
        try {
            return extractExpiration(SECRET, token).before(new Date());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

package com.sandeepprabhakula;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public class TokenGenerator {

    public String generateToken(String SECRET,String userName,Map<String,Object>claims,long expirationDuration){

        return createToken(SECRET,claims,userName,expirationDuration);
    }

    private String createToken(String SECRET, Map<String, Object> claims, String userName,long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getSignKey(SECRET), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey(String SECRET) {
        byte[]keyBytes = Decoders.BASE64URL.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public Date extractExpiration(String SECRET,String token) {
        return extractClaim(SECRET,token, Claims::getExpiration);
    }

    private <T> T extractClaim(String SECRET, String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(SECRET, token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String SECRET,String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey(SECRET))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String SECRET,String token) {
        return extractExpiration(SECRET,token).before(new Date());
    }
}

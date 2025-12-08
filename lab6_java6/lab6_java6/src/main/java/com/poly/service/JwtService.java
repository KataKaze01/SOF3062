package com.poly.service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    public String create(UserDetails user, int expirySeconds) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setClaims(Map.of("name", "Poly"))
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirySeconds * 1000L))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims getBody(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validate(Claims claims) {
        return claims.getExpiration().after(new Date());
    }

    Key getSigningKey() {
        String secret = "0123456789.0123456789.0123456789"; // tối thiểu 32 bytes
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}

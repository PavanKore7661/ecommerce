package com.pavan.ecommerce.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final String SECRET = "very_secret_key_change_later_very_long_secure_key_123456";

    private final long ACCESS_TOKEN_VALIDITY = 90 * 60 * 1000;

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateAccessToken(String email) {

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis()
                                + ACCESS_TOKEN_VALIDITY))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, String email) {

        String extracted = extractUsername(token);

        return extracted.equals(email)
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {

        Date expiry = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expiry.before(new Date());
    }

    public String getLoggedInUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}

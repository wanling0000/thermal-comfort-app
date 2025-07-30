package com.wanling.domain.user.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private static final long EXPIRATION = 24 * 60 * 60 * 300 * 1000L;
    private static final SecretKey SECRET = Keys.hmacShaKeyFor("YourSecretKeyYourSecretKey123456".getBytes(StandardCharsets.UTF_8));

    public String createToken(String userId, String username, String email, LocalDateTime createdAt) {
        return Jwts.builder()
                   .setSubject("USER_INFO")
                   .claim("userId", userId)
                   .claim("username", username)
                   .claim("email", email)
                   .claim("createdAt", createdAt.toString()) // ISO 格式
                   .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                   .signWith(SECRET)
                   .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

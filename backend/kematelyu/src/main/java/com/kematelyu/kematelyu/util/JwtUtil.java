package com.kematelyu.kematelyu.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    /* ✅ Simpan di ENV-VAR pada production */
    private static final String SECRET = "KemaTelyuSuperSecretKeyYangPanjangBangetBiar256bit!";
    private static final long   EXP_MS = 1000 * 60 * 60; // 1 jam

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(Long userId, String role) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + EXP_MS);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(exp)            // ⏰ expiry 1 jam
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}

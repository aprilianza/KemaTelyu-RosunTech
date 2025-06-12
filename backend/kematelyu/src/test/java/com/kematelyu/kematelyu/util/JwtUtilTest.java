package com.kematelyu.kematelyu.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private final JwtUtil jwt = new JwtUtil();

    @Test
    void generate_and_parse_token_roundtrip() {
        Long userId = 99L;
        String role = "MAHASISWA";

        String token = jwt.generateToken(userId, role);
        Claims body  = jwt.parse(token).getBody();

        assertEquals(String.valueOf(userId), body.getSubject());
        assertEquals(role, body.get("role", String.class));
        assertTrue(body.getExpiration().getTime() > System.currentTimeMillis());
    }
}

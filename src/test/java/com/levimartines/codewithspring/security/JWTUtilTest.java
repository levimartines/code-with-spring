package com.levimartines.codewithspring.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JWTUtilTest {

    @Autowired
    private JWTUtil jwtUtil;

    private final String username = "TEST";

    @Test
    void shouldTestTokenGeneration() {
        String token = jwtUtil.generateToken(username);
        assertNotNull(token);
    }

    @Test
    void isTokenValid() {
        String token = jwtUtil.generateToken(username);
        assertTrue(jwtUtil.isTokenValid(token));
        assertFalse(jwtUtil.isTokenValid(username));
        assertFalse(jwtUtil.isTokenValid(null));
    }

    @Test
    void getUsername() {
        String token = jwtUtil.generateToken(username);
        String tokenUsername = jwtUtil.getUsername(token);
        assertEquals(username, tokenUsername);
    }
}
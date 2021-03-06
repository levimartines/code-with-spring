package com.levimartines.codewithspring.security;

import com.levimartines.codewithspring.it.BaseIntegrationTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JWTUtilTest extends BaseIntegrationTest {

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
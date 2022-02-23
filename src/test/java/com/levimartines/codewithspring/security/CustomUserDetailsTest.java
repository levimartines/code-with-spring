package com.levimartines.codewithspring.security;

import com.levimartines.codewithspring.entities.model.User;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
class CustomUserDetailsTest {

    private User buildUser(boolean isAdmin) {
        var encoder = new BCryptPasswordEncoder();
        return User.builder()
            .name("Test")
            .email("test@test.com")
            .password(encoder.encode("test"))
            .isAdmin(isAdmin)
            .isActive(true)
            .build();
    }

    @Test
    void getAuthorities() {
        User user = buildUser(true);
        CustomUserDetails userDetails = new CustomUserDetails(user);
        Collection<?> authorities = userDetails.getAuthorities();
        assertNotNull(authorities);
        assertEquals(2, authorities.size());

        user = buildUser(false);
        userDetails = new CustomUserDetails(user);
        authorities = userDetails.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
    }

    @Test
    void getPassword() {
        User user = buildUser(true);
        CustomUserDetails userDetails = new CustomUserDetails(user);
        String password = userDetails.getPassword();
        assertNotNull(password);

        userDetails = new CustomUserDetails(null);
        password = userDetails.getPassword();
        assertNull(password);
    }

    @Test
    void getUsername() {
        User user = buildUser(true);
        CustomUserDetails userDetails = new CustomUserDetails(user);
        String username = userDetails.getUsername();
        assertNotNull(username);

        userDetails = new CustomUserDetails(null);
        username = userDetails.getUsername();
        assertNull(username);
    }

    @Test
    void isAccountNonExpired() {
        User user = buildUser(true);
        CustomUserDetails userDetails = new CustomUserDetails(user);
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void isAccountNonLocked() {
        User user = buildUser(true);
        CustomUserDetails userDetails = new CustomUserDetails(user);
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpired() {
        User user = buildUser(true);
        CustomUserDetails userDetails = new CustomUserDetails(user);
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void isEnabled() {
        User user = buildUser(true);
        CustomUserDetails userDetails = new CustomUserDetails(user);
        boolean isEnabled = userDetails.isEnabled();
        assertTrue(isEnabled);

        userDetails = new CustomUserDetails(null);
        isEnabled = userDetails.isEnabled();
        assertFalse(isEnabled);
    }

    @Test
    void isAdmin() {
        User user = buildUser(true);
        CustomUserDetails userDetails = new CustomUserDetails(user);
        boolean isAdmin = userDetails.isAdmin();
        assertTrue(isAdmin);

        user = buildUser(false);
        userDetails = new CustomUserDetails(user);
        isAdmin = userDetails.isAdmin();
        assertFalse(isAdmin);
    }

    @Test
    void getUser() {
        User user = buildUser(true);
        CustomUserDetails userDetails = new CustomUserDetails(user);
        assertEquals(user, userDetails.getUser());
    }
}
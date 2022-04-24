package com.levimartines.codewithspring.security;

import com.levimartines.codewithspring.entities.model.User;
import com.levimartines.codewithspring.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
class CustomUserDetailsServiceTest {

    @InjectMocks
    private CustomUserDetailsService service;

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldRetrieveUserDetailsWhenFindUser() {
        String email = "test@test.com";
        User user = User.builder().email(email).build();
        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);

        CustomUserDetails userDetails = (CustomUserDetails) service.loadUserByUsername(email);
        assertNotNull(userDetails);
    }

    @Test
    void shouldThrowAnExceptionWhenNotFindUser() {
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(""));
    }
}
package com.levimartines.codewithspring.it;

import com.levimartines.codewithspring.entities.vo.LoginVO;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpMethod.GET;

class AuthControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldReturn200AndAuthTokenWhenCredentialsMatch() {
        ResponseEntity<Void> response = login();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getHeaders().get("Authorization"));
        assertFalse(response.getHeaders().get("Authorization").isEmpty());
    }

    @Test
    void shouldReturn401WhenCredentialsDontMatch() {
        LoginVO form = new LoginVO(loggedUser.getEmail(), "wrongPassword");
        ResponseEntity<Void> response = restTemplate.postForEntity("/login", form, Void.class);
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void shouldReturn403WhenNotAuthorized() {
        ResponseEntity<Void> response = restTemplate.getForEntity("/actuator", Void.class);
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void shouldReturn200WhenAuthorized() {
        HttpEntity<?> entity = getEntity(null);
        ResponseEntity<Void> response = restTemplate.exchange("/actuator", GET, entity, Void.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}

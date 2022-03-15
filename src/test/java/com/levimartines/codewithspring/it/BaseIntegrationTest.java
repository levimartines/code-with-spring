package com.levimartines.codewithspring.it;

import com.levimartines.codewithspring.CodeWithSpringApplication;
import com.levimartines.codewithspring.entities.model.User;
import com.levimartines.codewithspring.entities.vo.LoginVO;
import com.levimartines.codewithspring.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {CodeWithSpringApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    protected TestRestTemplate restTemplate;

    protected User createUser() {
        User user = buildAdminUser();
        return userRepository.save(user);
    }

    private User buildAdminUser() {
        return User.builder()
            .name("admin")
            .email("admin@admin.com")
            .isAdmin(true)
            .isActive(true)
            .password(encoder.encode("admin"))
            .build();
    }

    protected HttpEntity<?> getEntity() {
        ResponseEntity<Void> response = login();
        String bearerToken = response.getHeaders().get("Authorization").get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", bearerToken);
        return new HttpEntity<>(headers);
    }

    protected ResponseEntity<Void> login() {
        User user = createUser();
        LoginVO form = new LoginVO(user.getEmail(), "admin");
        return restTemplate.postForEntity("/login", form, Void.class);
    }

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
    }
}

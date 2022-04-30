package com.levimartines.codewithspring.it;

import com.levimartines.codewithspring.CodeWithSpringApplication;
import com.levimartines.codewithspring.entities.model.User;
import com.levimartines.codewithspring.entities.vo.LoginVO;
import com.levimartines.codewithspring.repository.TaskRepository;
import com.levimartines.codewithspring.repository.UserRepository;

import java.util.Objects;

import org.junit.jupiter.api.AfterEach;
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
public abstract class BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    protected TestRestTemplate restTemplate;
    protected User loggedUser;

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

    protected <T> HttpEntity<?> getEntity(T t) {
        ResponseEntity<Void> response = login();
        String bearerToken = response.getHeaders().get("Authorization").get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", bearerToken);
        return new HttpEntity<>(t, headers);
    }

    protected ResponseEntity<Void> login() {
        User user = loggedUser;
        LoginVO form = new LoginVO(user.getEmail(), "admin");
        return restTemplate.postForEntity("/login", form, Void.class);
    }

    protected void setLoggedUserNoAdmin() {
        User user = loggedUser.toBuilder().isAdmin(false).build();
        loggedUser = user;
        userRepository.save(user);
    }

    @BeforeEach
    void beforeEach() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
        loggedUser = createUser();
    }
}

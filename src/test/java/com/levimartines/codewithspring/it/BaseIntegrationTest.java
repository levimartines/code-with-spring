package com.levimartines.codewithspring.it;

import com.levimartines.codewithspring.CodeWithSpringApplication;
import com.levimartines.codewithspring.entities.model.User;
import com.levimartines.codewithspring.entities.vo.LoginVO;
import com.levimartines.codewithspring.repository.TaskRepository;
import com.levimartines.codewithspring.repository.UserRepository;

import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;


@SpringBootTest(
    classes = {CodeWithSpringApplication.class},
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {BaseIntegrationTest.DockerMySQLDataSourceInitializer.class, BaseIntegrationTest.KafkaInitializer.class})
@Testcontainers
public abstract class BaseIntegrationTest {

    public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest");

    @ClassRule
    public static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    static {
        mySQLContainer.start();
        kafkaContainer.start();
    }

    public static class DockerMySQLDataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {

            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "spring.datasource.url=" + mySQLContainer.getJdbcUrl(),
                "spring.datasource.username=" + mySQLContainer.getUsername(),
                "spring.datasource.password=" + mySQLContainer.getPassword()
            );
        }
    }


    public static class KafkaInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {

            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "spring.kafka.producer.bootstrap-servers=" + kafkaContainer.getBootstrapServers()
            );
        }
    }

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

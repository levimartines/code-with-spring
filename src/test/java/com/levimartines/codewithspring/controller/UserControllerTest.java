package com.levimartines.codewithspring.controller;

import com.levimartines.codewithspring.entities.dto.UserDTO;
import com.levimartines.codewithspring.entities.model.User;
import com.levimartines.codewithspring.entities.vo.UserVO;
import com.levimartines.codewithspring.security.JWTUtil;
import com.levimartines.codewithspring.service.UserService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @MockBean
    private UserService userService;
    @MockBean
    private JWTUtil jwtUtil;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void findById() {
        String name = "Test";
        String email = "test@test.com";
        User user = User.builder().id(1L).name(name).email(email).build();
        when(userService.findById(Mockito.eq(1L))).thenReturn(user);
        restTemplate.getForEntity("/users/1", UserDTO.class);

        HttpEntity<UserVO> entity = new HttpEntity<>(createHeadersWithAuthorization(email));

        ResponseEntity<UserDTO> response = restTemplate.exchange("/users",
            HttpMethod.GET, entity, UserDTO.class);

        UserDTO body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getEmail()).isEqualTo(email);
    }

    @Test
    void save() {
        String name = "Test";
        String email = "test@test.com";
        User user = User.builder().id(1L).name(name).email(email).build();
        when(userService.create(Mockito.any())).thenReturn(user);
        UserVO userToSave = UserVO.builder().name(name).email(email).password("password").build();

        ResponseEntity<UserDTO> response = restTemplate.postForEntity("/users", userToSave, UserDTO.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        UserDTO body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo(1L);
        assertThat(body.getName()).isEqualTo(name);
        assertThat(body.getEmail()).isEqualTo(email);
    }

    private HttpHeaders createHeadersWithAuthorization(String email) {
        String token = jwtUtil.generateToken(email);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        return headers;
    }
}
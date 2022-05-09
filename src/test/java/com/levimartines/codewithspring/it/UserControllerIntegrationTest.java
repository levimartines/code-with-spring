package com.levimartines.codewithspring.it;

import com.levimartines.codewithspring.entities.dto.UserDTO;
import com.levimartines.codewithspring.entities.vo.UserVO;
import com.levimartines.codewithspring.handlers.StandardError;
import com.levimartines.codewithspring.handlers.ValidationError;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserControllerIntegrationTest extends BaseIntegrationTest {

    private final String endpoint = "/users";

    @Nested
    class Post {

        @Test
        void shouldReturn200AndCreateANewUser() {
            UserVO userVO = UserVO.builder().email("newuser@test.com").name("NewUser").password("test").build();
            UserDTO user = createUser(userVO);
            assertEquals(userVO.getName(), user.getName());
            assertEquals(userVO.getEmail(), user.getEmail());
        }

        @Test
        void shouldReturn422AndDontCreateANewUser() {
            UserVO userVO = UserVO.builder().email("").name("").password("").build();

            ResponseEntity<ValidationError> response = restTemplate.postForEntity(endpoint, userVO, ValidationError.class);

            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
            assertNotNull(response.getBody());
            ValidationError body = response.getBody();
            assertNotNull(body.getErrors());
            assertNotNull(body.getErrors().get("name"));
            assertNotNull(body.getErrors().get("email"));
            assertNotNull(body.getErrors().get("password"));
        }
    }

    @Nested
    class Get {
        @Test
        void shouldReturn200AndLoggedUserData() {
            String url = endpoint + "/" + loggedUser.getId();
            ResponseEntity<UserDTO> response = restTemplate.exchange(url, HttpMethod.GET, getEntity(null), UserDTO.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            UserDTO body = response.getBody();
            assertEquals(loggedUser.getEmail(), body.getEmail());
        }

        @Test
        void shouldReturn403AndNoUserDataWhenLoggedUserAndRequestedUserAreNotTheSame() {
            setLoggedUserNoAdmin();

            String url = endpoint + "/0";
            ResponseEntity<StandardError> response = restTemplate.exchange(url, HttpMethod.GET, getEntity(null), StandardError.class);
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Access denied", response.getBody().getMessage());
        }


        @Test
        void shouldReturn404WhenUserDoesNotExist() {
            String url = endpoint + "/99999999";
            ResponseEntity<StandardError> response = restTemplate.exchange(url, HttpMethod.GET, getEntity(null), StandardError.class);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNotNull(response.getBody());
        }

    }

    protected UserDTO createUser(UserVO userVO) {
        ResponseEntity<UserDTO> response = restTemplate.postForEntity(endpoint, userVO, UserDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return response.getBody();
    }
}

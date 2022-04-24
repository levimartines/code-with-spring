package com.levimartines.codewithspring.it;

import com.levimartines.codewithspring.entities.model.Task;
import com.levimartines.codewithspring.entities.vo.TaskVO;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class TaskControllerIntegrationTest extends BaseIntegrationTest {

    private final String endpoint = "/tasks";

    @Test
    void shouldReturn200AndCreateANewTask() {
        TaskVO taskVO = TaskVO.builder().name("Test").build();
        ResponseEntity<Task> response = restTemplate.exchange(endpoint, HttpMethod.POST, getEntity(taskVO), Task.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Task body = response.getBody();
        assertEquals(taskVO.getName(), body.getName());
    }

    @Test
    void shouldReturn403WhenUserIsNotAuthenticated() {
        TaskVO taskVO = TaskVO.builder().name("Test").build();

        ResponseEntity<Task> response = restTemplate.exchange(endpoint, HttpMethod.POST, new HttpEntity<>(taskVO), Task.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
    }
}

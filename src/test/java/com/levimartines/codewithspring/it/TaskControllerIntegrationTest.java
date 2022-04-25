package com.levimartines.codewithspring.it;

import com.levimartines.codewithspring.entities.model.Task;
import com.levimartines.codewithspring.entities.vo.TaskVO;
import com.levimartines.codewithspring.handlers.ValidationError;

import org.junit.jupiter.api.Nested;
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

    @Nested
    class Post {

        @Test
        void shouldReturn200AndCreateANewTask() {
            TaskVO taskVO = TaskVO.builder().name("Test").build();

            Task task = createTask(taskVO);
            assertEquals(taskVO.getName(), task.getName());
        }

        @Test
        void shouldReturn403WhenUserIsNotAuthenticated() {
            TaskVO taskVO = TaskVO.builder().name("Test").build();

            ResponseEntity<Task> response = restTemplate.exchange(endpoint, HttpMethod.POST, new HttpEntity<>(taskVO), Task.class);

            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            assertNull(response.getBody());
        }

        @Test
        void shouldReturn422AndDontCreateANewTask() {
            TaskVO taskVO = TaskVO.builder().name("").build();
            ResponseEntity<ValidationError> response = restTemplate.exchange(endpoint, HttpMethod.POST, getEntity(taskVO), ValidationError.class);

            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
            assertNotNull(response.getBody());
            ValidationError body = response.getBody();
            assertNotNull(body.getErrors());
            assertNotNull(body.getErrors().get("name"));
        }
    }

    @Nested
    class Get {
        @Test
        void shouldReturn200AndAllTasks(){
            createTask(TaskVO.builder().name("Task 1").build());
            createTask(TaskVO.builder().name("Task 2").build());
            createTask(TaskVO.builder().name("Task 3").build());

            ResponseEntity<Task[]> response = restTemplate.exchange(endpoint, HttpMethod.GET, getEntity(null), Task[].class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(3, response.getBody().length);
        }

    }

    protected Task createTask(TaskVO taskVO) {
        ResponseEntity<Task> response = restTemplate.exchange(endpoint, HttpMethod.POST, getEntity(taskVO), Task.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return response.getBody();
    }
}

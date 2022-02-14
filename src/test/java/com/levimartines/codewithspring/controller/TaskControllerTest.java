package com.levimartines.codewithspring.controller;

import com.levimartines.codewithspring.entities.dto.TaskDTO;
import com.levimartines.codewithspring.entities.model.Task;
import com.levimartines.codewithspring.entities.vo.TaskVO;
import com.levimartines.codewithspring.service.TaskService;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerTest {

    @MockBean
    private TaskService taskService;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void findAll() {
        Task t1 = Task.builder().id(1L).name("T1").done(false).build();
        Task t2 = Task.builder().id(2L).name("T2").done(false).build();
        when(taskService.findAll()).thenReturn(List.of(t1, t2));

        ResponseEntity<TaskDTO[]> response = restTemplate.getForEntity("/tasks", TaskDTO[].class);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        TaskDTO[] tasks = response.getBody();

        assertThat(tasks).isNotNull();
        assertThat(tasks).hasSize(2);

        assertThat(tasks[0].getName()).isEqualTo("T1");
        assertThat(tasks[0].getDone()).isFalse();
    }

    @Test
    void save() {
        Task t1 = Task.builder().id(1L).name("T1").done(false).build();
        when(taskService.create(Mockito.any())).thenReturn(t1);

        TaskVO taskToSave = TaskVO.builder().name("T1").done(false).build();
        ResponseEntity<TaskDTO> response = restTemplate.postForEntity("/tasks", taskToSave, TaskDTO.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        TaskDTO task = response.getBody();
        assertThat(task).isNotNull();
        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getName()).isEqualTo("T1");
        assertThat(task.getDone()).isFalse();
    }
}
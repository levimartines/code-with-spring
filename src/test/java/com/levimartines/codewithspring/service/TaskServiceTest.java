package com.levimartines.codewithspring.service;

import com.levimartines.codewithspring.entities.model.Task;
import com.levimartines.codewithspring.repository.TaskRepository;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(SpringExtension.class)
public class TaskServiceTest {

    @InjectMocks
    private TaskService service;

    @Mock
    private TaskRepository repository;

    @Test
    void shouldCallRepositoryFindAll() {
        Task t1 = Task.builder().id(1L).name("T1").done(false).build();
        Task t2 = Task.builder().id(2L).name("T2").done(false).build();
        Mockito.when(repository.findAll())
            .thenReturn(List.of(t1, t2));

        List<Task> tasks = service.findAll();

        Mockito.verify(repository, Mockito.times(1)).findAll();
        assertNotNull(tasks);
        assertEquals(2, tasks.size());
    }


    @Test
    void shouldCallRepositorySave() {
        Task t1 = Task.builder().name("T1").build();
        Mockito.when(repository.save(eq(t1)))
            .thenReturn(Task.builder().id(1L).name("T1").done(false).build());

        Task task = service.create(t1);

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.eq(t1));
        assertNotNull(task);
        assertEquals(1L, task.getId());
    }
}
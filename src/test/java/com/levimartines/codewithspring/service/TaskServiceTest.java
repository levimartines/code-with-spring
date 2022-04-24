package com.levimartines.codewithspring.service;

import com.levimartines.codewithspring.entities.model.Task;
import com.levimartines.codewithspring.entities.model.User;
import com.levimartines.codewithspring.repository.TaskRepository;
import com.levimartines.codewithspring.security.CustomUserDetails;
import com.levimartines.codewithspring.security.PrincipalService;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(SpringExtension.class)
class TaskServiceTest {

    @InjectMocks
    private TaskService service;

    @Mock
    private TaskRepository repository;

    private final User user = User.builder().id(1L).build();

    @Test
    void shouldCallRepositoryFindAll() {
        Task t1 = Task.builder().id(1L).name("T1").userId(user.getId()).done(false).build();
        Task t2 = Task.builder().id(2L).name("T2").userId(user.getId()).done(false).build();
        Mockito.when(repository.findAllByUserId(user.getId()))
            .thenReturn(List.of(t1, t2));

        try (MockedStatic<PrincipalService> mocked = Mockito.mockStatic(PrincipalService.class)) {
            mocked.when(PrincipalService::authenticated).thenReturn(new CustomUserDetails(user));

            List<Task> tasks = service.findAll();

            Mockito.verify(repository, Mockito.times(1)).findAllByUserId(user.getId());
            assertNotNull(tasks);
            assertEquals(2, tasks.size());
        }
    }


    @Test
    void shouldCallRepositorySave() {
        //given
        Task t1 = Task.builder().name("T1").build();
        Mockito.when(repository.save(t1))
            .thenReturn(Task.builder().id(1L).name("T1").done(false).build());

        try (MockedStatic<PrincipalService> mocked = Mockito.mockStatic(PrincipalService.class)) {
            mocked.when(PrincipalService::authenticated).thenReturn(new CustomUserDetails(user));

            //when
            Task task = service.create(t1);

            //then
            Mockito.verify(repository, Mockito.times(1)).save(t1);
            assertNotNull(task);
            assertEquals(1L, task.getId());
        }
    }
}
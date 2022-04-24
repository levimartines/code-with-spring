package com.levimartines.codewithspring.service;

import com.levimartines.codewithspring.entities.model.Task;
import com.levimartines.codewithspring.exceptions.AuthorizationException;
import com.levimartines.codewithspring.repository.TaskRepository;
import com.levimartines.codewithspring.security.CustomUserDetails;
import com.levimartines.codewithspring.security.PrincipalService;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;

    public List<Task> findAll() {
        CustomUserDetails userDetails = PrincipalService.authenticated();
        if (isNull(userDetails) || isNull(userDetails.getUser())) {
            throw new AuthorizationException("Need to be authenticated");
        }
        return repository.findAllByUserId(userDetails.getUser().getId());
    }

    public Task create(Task task) {
        task.setId(null);
        task.setDone(false);

        CustomUserDetails userDetails = PrincipalService.authenticated();
        if (isNull(userDetails) || isNull(userDetails.getUser())) {
            throw new AuthorizationException("Need to be authenticated");
        }
        task.setUserId(userDetails.getUser().getId());
        return save(task);
    }

    public Task save(Task task) {
        return repository.save(task);
    }
}

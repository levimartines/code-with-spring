package com.levimartines.codewithspring.service;

import com.levimartines.codewithspring.entities.model.Task;
import com.levimartines.codewithspring.repository.TaskRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;

    public List<Task> findAll() {
        return repository.findAll();
    }

    public Task create(Task task) {
        task.setId(null);
        return save(task);
    }

    public Task save(Task task) {
        return repository.save(task);
    }
}

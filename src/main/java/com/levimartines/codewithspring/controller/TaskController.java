package com.levimartines.codewithspring.controller;

import com.levimartines.codewithspring.entities.dto.TaskDTO;
import com.levimartines.codewithspring.entities.model.Task;
import com.levimartines.codewithspring.entities.vo.TaskVO;
import com.levimartines.codewithspring.service.TaskService;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final ModelMapper mapper;

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<TaskDTO> tasks = taskService.findAll().stream()
            .map(task -> mapper.map(task, TaskDTO.class))
            .collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<TaskDTO> save(@RequestBody @Valid TaskVO vo) {
        Task task = taskService.create(mapper.map(vo, Task.class));
        return ResponseEntity.ok(mapper.map(task, TaskDTO.class));
    }
}

package com.levimartines.codewithspring.repository;

import com.levimartines.codewithspring.entities.model.Task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByUserId(Long userId);
}

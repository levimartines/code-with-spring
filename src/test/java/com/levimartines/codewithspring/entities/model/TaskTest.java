package com.levimartines.codewithspring.entities.model;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class TaskTest {

    @Test
    void shouldTestBuilder() {
        Task task = Task.builder()
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        assertNotNull(task);
        assertNotNull(task.getCreatedAt());
        assertNotNull(task.getUpdatedAt());
        task.setCreatedAt(null);
        task.setUpdatedAt(null);
        assertNull(task.getCreatedAt());
        assertNull(task.getUpdatedAt());
    }


}
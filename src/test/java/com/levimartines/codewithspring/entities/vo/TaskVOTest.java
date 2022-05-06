package com.levimartines.codewithspring.entities.vo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class TaskVOTest {

    @Test
    void shouldTestBuilder() {
        TaskVO taskVO = TaskVO.builder().build();
        taskVO.setDone(true);
        taskVO.setName("TEST");
        assertNotNull(taskVO.getName());
        assertNotNull(taskVO.getDone());
    }

}
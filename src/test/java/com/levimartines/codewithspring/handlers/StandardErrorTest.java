package com.levimartines.codewithspring.handlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class StandardErrorTest {

    @Test
    void shouldTestGetters() {
        var error = new StandardError(1L, 1, "", "", "");
        assertNotNull(error.getTimestamp());
        assertNotNull(error.getStatus());
        assertNotNull(error.getError());
        assertNotNull(error.getMessage());
        assertNotNull(error.getPath());
    }

}
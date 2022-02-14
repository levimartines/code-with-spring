package com.levimartines.codewithspring.entities.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskDTO {
    private Long id;
    private String name;
    private Boolean done;
}

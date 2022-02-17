package com.levimartines.codewithspring.entities.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskDTO {
    private Long id;
    private String name;
    private Boolean done;
}

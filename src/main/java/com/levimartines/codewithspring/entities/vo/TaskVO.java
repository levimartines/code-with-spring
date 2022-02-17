package com.levimartines.codewithspring.entities.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TaskVO {

    @NotEmpty(message = "Name cannot be empty or null")
    @NotNull(message = "Name cannot be empty or null")
    private String name;

    @Builder.Default
    private Boolean done = false;
}

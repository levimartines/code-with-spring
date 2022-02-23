package com.levimartines.codewithspring.entities.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO implements Serializable {
    private Long id;
    private String name;
    private String email;
}

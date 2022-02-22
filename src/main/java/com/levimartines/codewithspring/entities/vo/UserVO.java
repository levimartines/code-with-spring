package com.levimartines.codewithspring.entities.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserVO {

    @NotEmpty(message = "Name cannot be empty or null")
    @NotNull(message = "Name cannot be empty or null")
    private String name;

    @NotEmpty(message = "Password cannot be empty or null")
    @NotNull(message = "Password cannot be empty or null")
    private String password;

    @NotEmpty(message = "Email cannot be empty or null")
    @NotNull(message = "Email cannot be empty or null")
    @Email(message = "Need to be a valid email")
    private String email;

}

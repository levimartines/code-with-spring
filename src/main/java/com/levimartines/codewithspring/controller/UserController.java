package com.levimartines.codewithspring.controller;

import com.levimartines.codewithspring.entities.dto.UserDTO;
import com.levimartines.codewithspring.entities.model.User;
import com.levimartines.codewithspring.entities.vo.UserVO;
import com.levimartines.codewithspring.service.UserService;

import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper mapper;

    @PostMapping
    public ResponseEntity<UserDTO> save(@RequestBody @Valid UserVO vo) {
        User user = userService.create(vo);
        return ResponseEntity.ok(mapper.map(user, UserDTO.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(mapper.map(user, UserDTO.class));
    }
}

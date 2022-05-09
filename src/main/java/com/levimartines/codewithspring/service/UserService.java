package com.levimartines.codewithspring.service;

import com.levimartines.codewithspring.entities.model.User;
import com.levimartines.codewithspring.entities.vo.UserVO;
import com.levimartines.codewithspring.exceptions.AuthorizationException;
import com.levimartines.codewithspring.exceptions.ObjectNotFoundException;
import com.levimartines.codewithspring.kafka.UserProducer;
import com.levimartines.codewithspring.repository.UserRepository;
import com.levimartines.codewithspring.security.CustomUserDetails;
import com.levimartines.codewithspring.security.PrincipalService;

import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserProducer producer;
    private final BCryptPasswordEncoder encoder;

    public User findById(Long id) {
        CustomUserDetails userDetails = PrincipalService.authenticated();
        if (userDetails == null || !userDetails.isAdmin() && !id
            .equals(userDetails.getUser().getId())) {
            throw new AuthorizationException("Access denied");
        }
        return repository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Object with id " + id + " not found. Type: " + User.class));
    }

    @Transactional
    public User create(UserVO vo) {
        User user = buildUserFromVo(vo);
        user = save(user);
        producer.send(user);
        return user;
    }

    public User save(User user) {
        return repository.save(user);
    }

    private User buildUserFromVo(UserVO vo) {
        return User.builder()
            .name(vo.getName())
            .password(encoder.encode(vo.getPassword()))
            .email(vo.getEmail())
            .isActive(true)
            .isAdmin(false)
            .build();
    }
}

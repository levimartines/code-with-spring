package com.levimartines.codewithspring.repository;

import com.levimartines.codewithspring.entities.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}

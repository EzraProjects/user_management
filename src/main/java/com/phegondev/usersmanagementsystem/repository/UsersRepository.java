package com.phegondev.usersmanagementsystem.repository;


import com.phegondev.usersmanagementsystem.entity.User;
import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<@Valid User, Integer> {

    Optional<User> findByUsername(String username);
}

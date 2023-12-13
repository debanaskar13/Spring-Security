package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);
    Optional<User> findByPrimaryEmail(String email);
    Optional<User> findByMobileNumber(String mobileNumber);
    Optional<User> findByResetPasswordToken(String token);

}


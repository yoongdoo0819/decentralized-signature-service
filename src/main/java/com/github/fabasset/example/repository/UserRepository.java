package com.github.fabasset.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.fabasset.example.model.User;

public interface UserRepository extends JpaRepository<User, String> {
}
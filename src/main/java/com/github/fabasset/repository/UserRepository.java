package com.github.fabasset.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.fabasset.model.User;

public interface UserRepository extends JpaRepository<User, String> {
}
package com.poscoict.posledger.assets.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poscoict.posledger.assets.model.User;

public interface UserRepository extends JpaRepository<User, String> {
}
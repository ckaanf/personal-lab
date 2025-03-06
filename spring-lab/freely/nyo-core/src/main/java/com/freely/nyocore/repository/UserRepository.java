package com.freely.nyocore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.freely.nyocore.core.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
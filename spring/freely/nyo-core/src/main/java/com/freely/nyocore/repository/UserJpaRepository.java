package com.freely.nyocore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.freely.nyocore.core.UserEntity;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
}
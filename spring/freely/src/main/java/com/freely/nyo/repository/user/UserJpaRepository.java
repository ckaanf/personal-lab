package com.freely.nyo.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.freely.nyo.core.UserEntity;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
}
package com.freely.nyocore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.freely.nyocore.core.CouponEntity;

public interface CouponJpaRepository extends JpaRepository<CouponEntity, Long> {
}
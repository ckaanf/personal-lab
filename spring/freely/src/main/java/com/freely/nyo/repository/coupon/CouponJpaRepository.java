package com.freely.nyo.repository.coupon;

import org.springframework.data.jpa.repository.JpaRepository;

import com.freely.nyo.core.CouponEntity;

public interface CouponJpaRepository extends JpaRepository<CouponEntity, Long> {
}
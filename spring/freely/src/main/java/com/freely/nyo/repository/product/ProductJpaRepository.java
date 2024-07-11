package com.freely.nyo.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;

import com.freely.nyo.core.ProductEntity;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
}
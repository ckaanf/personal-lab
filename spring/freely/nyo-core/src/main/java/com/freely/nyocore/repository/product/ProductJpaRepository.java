package com.freely.nyocore.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;

import com.freely.nyocore.core.ProductEntity;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
}
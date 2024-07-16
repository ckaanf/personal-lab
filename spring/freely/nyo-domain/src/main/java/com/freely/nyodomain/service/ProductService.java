package com.freely.nyodomain.service;

import org.springframework.stereotype.Service;

import com.freely.nyocore.repository.ProductJpaRepository;
import com.freely.nyodomain.domain.Product;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService {
	private final ProductJpaRepository productJpaRepository;

	public Product getProduct(long productId) {
		return Product.from(
			productJpaRepository.findById(productId).orElseThrow(EntityNotFoundException::new));
	}

	public void saveProduct(Product product) {
		productJpaRepository.save(Product.to(product));
	}
}


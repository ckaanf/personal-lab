package com.freely.nyo.service;

import org.springframework.stereotype.Service;

import com.freely.nyo.domain.Product;
import com.freely.nyocore.repository.product.ProductJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService {
	private final ProductJpaRepository productJpaRepository;

	public void getProduct(long productId) {
		productJpaRepository.findById(productId);
	}

	public void saveProduct(Product product) {
		productJpaRepository.save(Product.to(product));
	}
}

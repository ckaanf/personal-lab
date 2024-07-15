package com.freely.nyodomain.service;

import org.springframework.stereotype.Service;

import com.freely.nyodomain.domain.Product;
import com.freely.nyodomain.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService {
	private final ProductRepository productRepository;

	public Product getProduct(long productId) {
		return productRepository.findById(productId);
	}

	public void saveProduct(Product product) {
		productRepository.save(product);
	}
}

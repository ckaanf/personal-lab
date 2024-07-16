package com.freely.nyodomain.service;

import org.springframework.stereotype.Service;

import com.freely.nyocore.core.ProductEntity;
import com.freely.nyocore.repository.ProductJpaRepository;
import com.freely.nyodomain.domain.Product;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService {
	private final ProductJpaRepository productJpaRepository;

	public Product getProduct(long productId) {
		ProductEntity productEntity = productJpaRepository.findById(productId)
			.orElseThrow(() -> new EntityNotFoundException("Product not found"));

		return Product.builder()
			.id(productEntity.getId())
			.name(productEntity.getName())
			.price(productEntity.getPrice())
			.build();
	}

	public void saveProduct(Product product) {
		ProductEntity productEntity = new ProductEntity(
			product.getId(),
			product.getName(),
			product.getPrice()
		);
		productJpaRepository.save(productEntity);
	}
}


package com.freely.nyocore.repository.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.freely.nyodomain.domain.Product;
import com.freely.nyodomain.repository.ProductRepository;
import com.freely.nyocore.core.ProductEntity;

import jakarta.persistence.EntityNotFoundException;

@Repository
public class ProductEntityRepository implements ProductRepository {
	private final ProductJpaRepository productJpaRepository;

	public ProductEntityRepository(ProductJpaRepository productJpaRepository) {
		this.productJpaRepository = productJpaRepository;
	}

	@Override
	public Product findById(long productId) {
		ProductEntity productEntity = productJpaRepository.findById(productId)
				.orElseThrow(() -> new EntityNotFoundException("Product not found"));

		return Product.builder()
			.id(productEntity.getId())
			.name(productEntity.getName())
			.price(productEntity.getPrice())
			.build();
	}

	@Override
	public void save(Product product) {
		ProductEntity productEntity = new ProductEntity(
			product.getId(),
			product.getName(),
			product.getPrice()
		);
		productJpaRepository.save(productEntity);
	}
}

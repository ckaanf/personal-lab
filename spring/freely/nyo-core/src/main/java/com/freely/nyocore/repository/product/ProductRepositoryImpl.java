package com.freely.nyocore.repository.product;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Repository;

import com.freely.nyo.core.ProductEntity;
import com.freely.nyo.domain.Product;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

	private final ProductJpaRepository productJpaRepository;

	@Override
	public Product findById(long id) throws ChangeSetPersister.NotFoundException {
		return productJpaRepository.findById(id)
			.orElseThrow(ChangeSetPersister.NotFoundException::new)
			.toModel();
	}

	@Override
	public void save(Product product) {
		productJpaRepository.save(ProductEntity.from(product));

	}
}

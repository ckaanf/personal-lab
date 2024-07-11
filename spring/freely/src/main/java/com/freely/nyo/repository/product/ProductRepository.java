package com.freely.nyo.repository.product;

import org.springframework.data.crossstore.ChangeSetPersister;

import com.freely.nyo.domain.Product;

public interface ProductRepository {
	Product findById(long id) throws ChangeSetPersister.NotFoundException;
	void save(Product product);
}

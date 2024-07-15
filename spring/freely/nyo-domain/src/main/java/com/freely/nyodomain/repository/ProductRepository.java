package com.freely.nyodomain.repository;

import com.freely.nyodomain.domain.Product;

public interface ProductRepository {

	Product findById(long productId);

	void save(Product product);
}

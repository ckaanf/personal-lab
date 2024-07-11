package com.freely.nyo.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.crossstore.ChangeSetPersister;

import com.freely.nyo.domain.Product;
import com.freely.nyo.repository.product.ProductRepository;

@DataJpaTest
class ProductServiceTest {

	@MockBean
	private ProductRepository productRepository;

	@Test
	void createProduct() {

		Product stub = Product.builder().
				id(1L).
				name("name").
				price(1000).
				build();

		productRepository.save(stub);
		assertNotNull(stub);
	}

	@Test
	void test() throws ChangeSetPersister.NotFoundException {
		Product product = productRepository.findById(1L);
		assertEquals(product, product);
		System.out.println("product = " + product);
	}
}
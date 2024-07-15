package com.freely.nyo.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.freely.nyodomain.domain.Product;
import com.freely.nyodomain.repository.ProductRepository;
import com.freely.nyodomain.service.ProductService;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
	@InjectMocks
	ProductService productService;

	@Mock
	ProductRepository productRepository;

	@DisplayName("상품 저장")
	@Test
	void givenProductWhenThenReturnSaveProductEntity() {
		// given
		Product product = Product.builder()
			.name("상품")
			.price(1000)
			.build();

		// when
		// when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);
		productService.saveProduct(product);

		// then
		verify(productRepository, times(1)).save(product);
		// ProductEntity capturedProductEntity = productEntityCaptor.getValue();

		assertThat(product.getName()).isEqualTo(product.getName());
		assertThat(product.getPrice()).isEqualTo(product.getPrice());
	}

	@DisplayName("상품 조회")
	@Test
	void givenProductIdWhenThenReturnProduct() throws InstantiationException, IllegalAccessException {
		//given
		long productId = 1L;

		//when
		when(productRepository.findById(productId)).thenReturn(Product.builder().build());
		productService.getProduct(productId);

		//then
		verify(productRepository, times(1)).findById(productId);
		// Optional<ProductEntity> product = productJpaRepository.findById(productId);
		Optional<Product> product = Optional.ofNullable(productRepository.findById(productId));
		assertThat(product).isPresent();
	}
}
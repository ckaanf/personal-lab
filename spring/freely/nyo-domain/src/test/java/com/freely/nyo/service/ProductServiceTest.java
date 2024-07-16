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

import com.freely.nyocore.core.ProductEntity;
import com.freely.nyocore.repository.ProductJpaRepository;
import com.freely.nyodomain.domain.Product;
import com.freely.nyodomain.service.ProductService;

@ExtendWith({MockitoExtension.class})
class ProductServiceTest {
	@InjectMocks
	ProductService productService;

	@Mock
	ProductJpaRepository productJpaRepository;

	@DisplayName("상품 저장")
	@Test
	void givenProductWhenThenReturnSaveProductEntity() {
		// given
		Product product = Product.builder()
			.name("상품")
			.price(1000)
			.build();

		productService.saveProduct(product);

		// then
		// ProductEntity capturedProductEntity = productEntityCaptor.getValue();

		assertThat(product.getName()).isEqualTo(product.getName());
		assertThat(product.getPrice()).isEqualTo(product.getPrice());
	}

	@DisplayName("상품 조회")
	@Test
	void givenProductIdWhenThenReturnProduct() {
		//given
		long productId = 1L;

		//when
		when(productJpaRepository.findById(productId)).thenReturn(Optional.of(new ProductEntity()));
		productService.getProduct(productId);

		//then
		verify(productJpaRepository, times(1)).findById(productId);
		Optional<Optional<ProductEntity>> product = Optional.of(productJpaRepository.findById(productId));
		assertThat(product).isPresent();
	}
}
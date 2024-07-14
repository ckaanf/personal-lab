package com.freely.nyo.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.freely.nyo.domain.Product;
import com.freely.nyocore.core.ProductEntity;
import com.freely.nyocore.repository.product.ProductJpaRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
	@InjectMocks
	ProductService productService;

	@Mock
	ProductJpaRepository productJpaRepository;

	@Captor
	private ArgumentCaptor<ProductEntity> productEntityCaptor;

	@DisplayName("상품 저장")
	@Test
	void givenProductWhenThenReturnSaveProductEntity() {
		// given
		Product product = Product.builder()
			.name("상품")
			.price(1000)
			.build();

		ProductEntity productEntity = Product.to(product);

		// when
		when(productJpaRepository.save(any(ProductEntity.class))).thenReturn(productEntity);
		productService.saveProduct(product);

		// then
		verify(productJpaRepository, times(1)).save(productEntityCaptor.capture());
		ProductEntity capturedProductEntity = productEntityCaptor.getValue();

		assertThat(capturedProductEntity.getName()).isEqualTo(product.getName());
		assertThat(capturedProductEntity.getPrice()).isEqualTo(product.getPrice());
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
		Optional<ProductEntity> product = productJpaRepository.findById(productId);
		assertThat(product).isPresent();
	}
}
package com.freely.nyodomain.domain;

import com.freely.nyocore.core.ProductEntity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Product {
	private Long id;
	private String name;
	private int price;

	public static Product from(ProductEntity productEntity) {
		return Product.builder()
			.id(productEntity.getId())
			.name(productEntity.getName())
			.price(productEntity.getPrice())
			.build();
	}

	public static ProductEntity to (Product product) {
		return new ProductEntity(
			product.name,
			product.price
		);
	}
}

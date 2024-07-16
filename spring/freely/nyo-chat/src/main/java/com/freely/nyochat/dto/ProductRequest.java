package com.freely.nyochat.dto;

import com.freely.nyodomain.domain.Product;

public record ProductRequest(
	String name,
	int price
) {
	public static Product to(ProductRequest productRequest) {
		return Product.builder()
			.name(productRequest.name())
			.price(productRequest.price())
			.build();
	}
}

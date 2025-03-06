package com.freely.nyo.dto;

import com.freely.nyodomain.domain.Product;

import lombok.Builder;

@Builder
public record ProductResponse(
	Long id,
	String name,
	int price
) {
	public static ProductResponse from(Product product) {
		return ProductResponse.builder()
			.id(product.getId())
			.name(product.getName())
			.price(product.getPrice())
			.build();

	}
}

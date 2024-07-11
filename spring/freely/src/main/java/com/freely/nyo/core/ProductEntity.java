package com.freely.nyo.core;

import com.freely.nyo.domain.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
@Entity(name = "product")
public class ProductEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 100)
	private String name;
	private int price;

	public static ProductEntity from(Product product) {
		ProductEntity productEntity = new ProductEntity();
		productEntity.name = product.getName();
		productEntity.price = product.getPrice();
		return productEntity;
	}

	public Product toModel() {
		return Product.builder()
				.id(id)
				.name(name)
				.price(price)
				.build();
	}
}

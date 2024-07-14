package com.freely.nyocore.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity(name = "product")
public class ProductEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 100)
	private String name;
	private int price;

	public ProductEntity() {

	}

	public ProductEntity(Long id, String name, int price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}
}

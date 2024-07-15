package com.freely.nyodomain.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Product {
	private Long id;
	private String name;
	private int price;
}

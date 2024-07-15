package com.freely.nyodomain.domain;

import lombok.Builder;
import lombok.Getter;
@Builder
@Getter
public class User {
	private Long id;
	private String name;
	private int mileage;
}

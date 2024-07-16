package com.freely.nyodomain.domain.common;

import lombok.Builder;
import lombok.Getter;
@Builder
@Getter
public class User {
	private Long id;
	private String name;
	private int mileage;
}

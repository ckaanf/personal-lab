package com.freely.nyodomain.domain;

import lombok.Builder;
import lombok.Getter;
@Builder
@Getter
public class Coupon {
	private Long id;
	private User user;
	private int discount;
}

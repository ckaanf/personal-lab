package com.freely.nyo.core;

import com.freely.nyo.domain.Coupon;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name ="coupon")
public class CouponEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;
	private int discount;

	public static CouponEntity from(Coupon coupon) {
		CouponEntity couponEntity = new CouponEntity();
		couponEntity.id = coupon.getId();
		couponEntity.user = UserEntity.from(coupon.getUser());
		couponEntity.discount = coupon.getDiscount();
		return couponEntity;
	}

	public Coupon toModel() {
		return Coupon.builder()
				.id(id)
				.user(user.toModel())
				.discount(discount)
				.build();
	}
}

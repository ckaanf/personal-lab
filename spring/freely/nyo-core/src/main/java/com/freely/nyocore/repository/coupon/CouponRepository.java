package com.freely.nyocore.repository.coupon;

import org.springframework.data.crossstore.ChangeSetPersister;

import com.freely.nyo.domain.Coupon;

public interface CouponRepository {
	public Coupon findById(long id) throws ChangeSetPersister.NotFoundException;

	public void save(Coupon coupon);
}

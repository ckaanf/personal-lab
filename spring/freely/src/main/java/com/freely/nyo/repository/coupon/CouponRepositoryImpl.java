package com.freely.nyo.repository.coupon;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.freely.nyo.core.CouponEntity;
import com.freely.nyo.domain.Coupon;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

	private final CouponJpaRepository couponJpaRepository;

	@Override
	public Coupon findById(long id) throws ChangeSetPersister.NotFoundException {
		return couponJpaRepository.findById(id)
			.orElseThrow(ChangeSetPersister.NotFoundException::new)
			.toModel();
	}

	@Override
	@Transactional
	public void save(Coupon coupon) {
		couponJpaRepository.save(CouponEntity.from(coupon));
	}
}

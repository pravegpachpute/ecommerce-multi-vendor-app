package com.praveg.repository;

import com.praveg.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepo extends JpaRepository<Coupon, Long> {

    Coupon findByCode(String code);
}

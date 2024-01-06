package com.shop.repository.custom;

import com.shop.domain.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponConfig {
    Page<Coupon> selectCouponPage(Pageable pageable, String searchStr);
}

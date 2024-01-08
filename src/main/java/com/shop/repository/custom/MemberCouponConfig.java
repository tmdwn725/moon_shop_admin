package com.shop.repository.custom;

import com.shop.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberCouponConfig {
    public Page<Member> selectMemberCoupon(Pageable pageable, Long couponSeq);
}

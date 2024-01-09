package com.shop.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.domain.Coupon;
import com.shop.domain.QCoupon;
import com.shop.repository.custom.CouponConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponConfig {
    private final JPAQueryFactory queryFactory;
    QCoupon qCoupon = QCoupon.coupon;
    /**
     * 쿠폰 목록 조회
     * @param pageable
     * @param searchStr
     * @return
     */
    public Page<Coupon> selectCouponPage(Pageable pageable, String searchStr){
        QueryResults<Coupon> couponList = queryFactory
                .selectFrom(qCoupon)
                .where(searchCouponName(searchStr))
                .orderBy(qCoupon.useEdDate.desc()) // 최신순으로 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()) // 최대 8개만 조회
                .fetchResults();
        List<Coupon> content = couponList.getResults();
        long total = couponList.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression searchCouponName(String searchStr) {
        if (searchStr == null) {
            return null;
        }
        return qCoupon.couponName.contains(searchStr);
    }

    /**
     * 쿠폰 정보 수정
     * @param coupon
     */
    public void updateCouponInfo(Coupon coupon) {
        queryFactory.update(qCoupon)
                .set(qCoupon.couponName, coupon.getCouponName())
                .set(qCoupon.discRate, coupon.getDiscRate())
                .set(qCoupon.minPrice, coupon.getMinPrice())
                .set(qCoupon.maxDiscPrice, coupon.getMaxDiscPrice())
                .set(qCoupon.useStDate, coupon.getUseStDate())
                .set(qCoupon.useEdDate, coupon.getUseEdDate())
                .execute();
    }
}

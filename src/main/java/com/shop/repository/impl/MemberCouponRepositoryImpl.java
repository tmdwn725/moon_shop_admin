package com.shop.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.domain.Member;
import com.shop.domain.QMember;
import com.shop.domain.QMemberCoupon;
import com.shop.dto.enums.Role;
import com.shop.repository.custom.MemberCouponConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberCouponRepositoryImpl implements MemberCouponConfig {
    private final JPAQueryFactory queryFactory;
    QMemberCoupon qMemberCoupon = QMemberCoupon.memberCoupon;
    QMember qMember = QMember.member;
    public Page<Member> selectMemberCoupon(Pageable pageable, Long couponSeq) {
        QueryResults<Member> memberList =  queryFactory.selectFrom(qMember)
                .leftJoin(qMember.memberCouponList, qMemberCoupon)
                .on(qMemberCoupon.coupon.couponSeq.eq(couponSeq))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()) // 최대 8개만 조회
                .fetchResults();

        List<Member> content = memberList.getResults();
        long total = memberList.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
}

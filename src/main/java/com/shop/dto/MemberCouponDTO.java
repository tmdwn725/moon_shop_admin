package com.shop.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.shop.domain.Coupon;
import com.shop.domain.Member;
import lombok.Data;

import javax.persistence.*;

@Data
public class MemberCouponDTO {
    private Long memberCouponSeq;
    private MemberDTO member;
    private CouponDTO coupon;
    private String useYn;
    private Long[] memberSeqArray;
    private Long[] memberCouponSeqArray;
    private Long memberSeq;
    private Long couponSeq;
}

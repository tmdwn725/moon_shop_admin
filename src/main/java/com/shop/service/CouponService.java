package com.shop.service;

import com.shop.common.ModelMapperUtil;
import com.shop.domain.Coupon;
import com.shop.domain.Member;
import com.shop.domain.MemberCoupon;
import com.shop.dto.CouponDTO;
import com.shop.dto.MemberCouponDTO;
import com.shop.dto.MemberDTO;
import com.shop.repository.CouponRepository;
import com.shop.repository.MemberCouponRepository;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final MemberRepository memberRepository;
    /**
     * 쿠폰 목록 조회
     * @param start
     * @param limit
     * @param searchStr
     * @return
     */
    public Page<CouponDTO> selectCouponList(int start, int limit, String searchStr) {
        PageRequest pageRequest = PageRequest.of(start-1, limit);
        Page<Coupon> result = couponRepository.selectCouponPage(pageRequest, searchStr);
        int total = result.getTotalPages();
        pageRequest = PageRequest.of((total-1), limit);
        List<CouponDTO> list = ModelMapperUtil.mapAll(result.getContent(), CouponDTO.class);
        return new PageImpl<>(list, pageRequest, total);
    }
    /**
     * 쿠폰 상세정보 조회
     * @param couponSeq
     * @return
     */
    public CouponDTO selectCouponInfo(Long couponSeq) {
        Coupon couponInfo = couponRepository.findById(couponSeq).get();
        CouponDTO coupon = ModelMapperUtil.map(couponInfo, CouponDTO.class);
        return coupon;
    }
    /**
     * 쿠폰 정보 저장
     * @param couponDTO
     */
    public void saveCouponInfo(CouponDTO couponDTO){
        LocalDateTime nowDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate stDate = LocalDate.parse(couponDTO.getUseStDateStr(), formatter);
        LocalDate edDate = LocalDate.parse(couponDTO.getUseEdDateStr(), formatter);

        Coupon coupon = new Coupon();
        coupon.createCoupon(couponDTO.getCouponSeq(), couponDTO.getCouponName(), couponDTO.getDiscRate(), couponDTO.getMinPrice()
                , couponDTO.getMaxDiscPrice(), stDate, edDate, nowDateTime);

        couponRepository.save(coupon);
    }
    /**
     * 쿠폰 사용자 목록 조회
     * @param start
     * @param limit
     * @param couponSeq
     * @param searchStr
     * @return
     */
    public Page<MemberDTO> selectMemberCouponList(int start, int limit, Long couponSeq, String searchStr){
        PageRequest pageRequest = PageRequest.of(start-1, limit);
        Page<Member> result = memberCouponRepository.selectMemberCoupon(pageRequest, couponSeq);
        int total = result.getTotalPages();
        pageRequest = PageRequest.of((total-1), limit);
        List<MemberDTO> list = ModelMapperUtil.mapAll(result.getContent(), MemberDTO.class);

        list.forEach(member -> {
            List<MemberCouponDTO> memberCoupons = member.getMemberCouponList().stream()
                    .filter(memberCoupon -> memberCoupon.getCoupon().getCouponSeq() == couponSeq)
                    .collect(Collectors.toList());
            member.setMemberCouponList(memberCoupons);
        });
        return new PageImpl<>(list, pageRequest, total);
    }

    /**
     * 사용자 쿠폰 발급
     * @param memberCouponDTO
     */
    public void saveMemberCoupon(MemberCouponDTO memberCouponDTO){
        List<MemberCoupon> list = new ArrayList<MemberCoupon>();
        Coupon coupon = couponRepository.findById(memberCouponDTO.getCouponSeq()).get();
        for(Long memberSeq : memberCouponDTO.getMemberSeqArray()){
            Member member = memberRepository.findById(memberSeq).get();
            MemberCoupon memberCoupon = new MemberCoupon();
            memberCoupon.createMemberCoupon(member, coupon, "N");
            list.add(memberCoupon);
        }
        memberCouponRepository.saveAll(list);
    }

    /**
     * 사용자 쿠폰 발급취소
     * @param memberCouponDTO
     */
    public void removeMemberCoupon(MemberCouponDTO memberCouponDTO){
        memberCouponRepository.deleteAllById(Arrays.asList(memberCouponDTO.getMemberCouponSeqArray()));
    }

}

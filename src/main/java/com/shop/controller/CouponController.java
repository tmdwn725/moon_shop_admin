package com.shop.controller;

import com.shop.dto.CouponDTO;
import com.shop.dto.MemberCouponDTO;
import com.shop.dto.MemberDTO;
import com.shop.service.CouponService;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class CouponController {
    private final CouponService couponService;
    private final MemberService memberService;
    /**
     * 쿠폰 목록 조회
     * @param page
     * @param searchStr
     * @param model
     * @return
     */
    @GetMapping("/couponList")
    public String couponList(@RequestParam(value="page",required = false, defaultValue="1") int page
            , @RequestParam(value="searchStr",required = false, defaultValue="") String searchStr, Model model) {
        Page<CouponDTO> couponList = couponService.selectCouponList(page,6, searchStr);
        model.addAttribute("currentPage", page);
        model.addAttribute("couponList", couponList);
        return "coupon/couponList";
    }
    /**
     * 쿠폰 상세 정보 조회
     * @param model
     * @param coupon
     * @return
     */
    @RequestMapping("/couponInfo")
    public String couponInfo(Model model, CouponDTO coupon, @RequestParam(value="page",required = false, defaultValue="1") int page
            , @RequestParam(value="searchStr",required = false, defaultValue="") String searchStr
            , @RequestParam(value="", required = false, defaultValue = "tab-1") String tab){
        CouponDTO couponDTO = new CouponDTO();
        Page<MemberDTO> memberList = null;

        if(coupon.getCouponSeq() > 0){
            couponDTO = couponService.selectCouponInfo(coupon.getCouponSeq());
            memberList = couponService.selectMemberCouponList(page,6, coupon.getCouponSeq(), searchStr);
        }
        model.addAttribute("tab",tab);
        model.addAttribute("coupon", couponDTO);
        model.addAttribute("memberList", memberList);
        return "coupon/couponInfo";
    }
    /**
     * 쿠폰 정보 저장
     * @param couponDTO
     * @return
     * @throws IOException
     */
    @RequestMapping("/saveCouponInfo")
    public ResponseEntity<Void> saveCouponInfo(CouponDTO couponDTO) throws IOException {
        couponService.saveCouponInfo(couponDTO);
        return ResponseEntity.ok().build();
    }
    /**
     * 사용자 쿠폰 발급
     * @param memberCouponDTO
     * @return
     */
    @PostMapping("/saveMemberCoupon")
    public ResponseEntity<Void> saveMemberCoupon(MemberCouponDTO memberCouponDTO) {
        couponService.saveMemberCoupon(memberCouponDTO);
        return ResponseEntity.ok().build();
    }
    /**
     * 사용자 쿠폰 발급 취소
     * @param memberCouponDTO
     * @return
     */
    @DeleteMapping("/removeMemberCoupon")
    public ResponseEntity<Void> removeMemberCoupon(MemberCouponDTO memberCouponDTO) {
        couponService.removeMemberCoupon(memberCouponDTO);
        return ResponseEntity.ok().build();
    }
}

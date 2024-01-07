package com.shop.controller;

import com.shop.dto.CouponDTO;
import com.shop.dto.MemberDTO;
import com.shop.service.CouponService;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
            , @RequestParam(value="searchStr",required = false, defaultValue="") String searchStr){
        CouponDTO couponDTO = new CouponDTO();
        Page<MemberDTO> memberList = null;

        if(coupon.getCouponSeq() > 0){
            couponDTO = couponService.selectCouponInfo(coupon.getCouponSeq());
            memberList = couponService.selectMemberCouponList(page,6, coupon.getCouponSeq(), "");
        }
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
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        MemberDTO member = memberService.selectMemberById(memberId);
        couponService.saveCouponInfo(couponDTO);
        return ResponseEntity.ok().build();
    }
}

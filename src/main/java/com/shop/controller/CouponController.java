package com.shop.controller;

import com.shop.dto.CouponDTO;
import com.shop.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class CouponController {
    private final CouponService couponService;
    /**
     * 쿠폰 목록 조회
     * @param page
     * @param searchStr
     * @param model
     * @return
     */
    @GetMapping("/couponList")
    public String getCouponList(@RequestParam(value="page",required = false, defaultValue="1") int page
            , @RequestParam(value="searchStr",required = false, defaultValue="") String searchStr, Model model) {
        Page<CouponDTO> couponList = couponService.selectCouponList(page,6, searchStr);
        model.addAttribute("currentPage", page);
        model.addAttribute("couponList", couponList);
        return "coupon/couponList";
    }
}

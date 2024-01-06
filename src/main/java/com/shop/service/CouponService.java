package com.shop.service;

import com.shop.common.ModelMapperUtil;
import com.shop.domain.Coupon;
import com.shop.dto.CouponDTO;
import com.shop.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;

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

}

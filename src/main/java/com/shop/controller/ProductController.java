package com.shop.controller;

import com.shop.dto.CouponDTO;
import com.shop.dto.MemberCouponDTO;
import com.shop.dto.MemberDTO;
import com.shop.dto.ProductDTO;
import com.shop.dto.enums.ProductType;
import com.shop.service.CouponService;
import com.shop.service.MemberService;
import com.shop.service.ProductService;
import com.shop.service.ProductStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final MemberService memberService;
    private final ProductService productService;
    private final ProductStockService productStockService;
    private final CouponService couponService;
    @RequestMapping("/productList")
    public String productList(@RequestParam(value="page",required = false, defaultValue="1") int page
            , @RequestParam(value="type",required = false, defaultValue="") String type
            , @RequestParam(value="searchStr",required = false, defaultValue="") String searchStr, Model model) {
        ProductType productType = null;
        if(type.length() > 0){
            productType = ProductType.of(type);
        }
        Page<ProductDTO> productList = productService.selectProductList(page,6, productType, searchStr);
        model.addAttribute("currentPage", page);
        model.addAttribute("productList", productList);
        model.addAttribute("productType", Arrays.asList(ProductType.values()));
        model.addAttribute("type",type);
        return "product/productList";
    }

}

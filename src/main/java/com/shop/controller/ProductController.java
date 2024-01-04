package com.shop.controller;

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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final MemberService memberService;
    private final ProductService productService;
    private final ProductStockService productStockService;
    private final CouponService couponService;
    @GetMapping("/productList")
    public String productList(@RequestParam(value="page",required = false, defaultValue="1") int page
            , @RequestParam(value="type",required = false, defaultValue="") String type
            , @RequestParam(value="searchStr",required = false, defaultValue="") String searchStr, Model model) {
        ProductType productType = null;
        if(type.length() > 0){
            productType = ProductType.of(type);
        }
        Page<ProductDTO> productList = productService.selectProductList(page,6, productType, searchStr);
        model.addAttribute("searchStr", searchStr);
        model.addAttribute("currentPage", page);
        model.addAttribute("productList", productList);
        model.addAttribute("productType", Arrays.asList(ProductType.values()));
        model.addAttribute("type",type);
        return "product/productList";
    }
    /**
     * 상품상세정보 조회
     * @param model
     * @param product
     * @return
     */
    @GetMapping("/productInfo")
    public String productInfo(Model model, ProductDTO product){
        ProductDTO productDTO = new ProductDTO();
        ProductType myProductType = ProductType.CARDIGAN;

        if(product.getProductSeq() > 0){
            productDTO = productService.selectProductInfo(product.getProductSeq());
            myProductType = productDTO.getProductType();
        }

        model.addAttribute("product", productDTO);
        model.addAttribute("myProductType",myProductType);
        model.addAttribute("productType", Arrays.asList(ProductType.values()));
        return "product/productInfo";
    }
    /**
     * 상품 등록
     * @param productDTO
     * @return
     */
    @PostMapping("/saveProduct")
    public ResponseEntity<Void> saveProduct(ProductDTO productDTO
            , @RequestParam("file-img1") MultipartFile file1, @RequestParam("file-img2") MultipartFile file2
            , @RequestParam("file-img3") MultipartFile file3, @RequestParam("file-img4") MultipartFile file4) throws IOException {
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        MemberDTO member = memberService.selectMemberById(memberId);
        productDTO.setProductType(ProductType.of(productDTO.getProductTypeCd()));
        productDTO.setSellerSeq(member.getMemberSeq());
        MultipartFile[] fileList = {file1, file2, file3, file4};
        productService.saveProductInfo(productDTO, fileList);
        return ResponseEntity.ok().build();
    }
    /**
     * 상품 삭제
     * @param productSeq
     * @return
     */
    @DeleteMapping("/removeProduct")
    public ResponseEntity<Void> removeProduct(@RequestParam("productSeq") Long productSeq) {
        productService.removeProduct(productSeq);
        return ResponseEntity.ok().build();
    }
    /**
     * 상품 재고 삭제
     * @param product
     * @return
     */
    @DeleteMapping("/removeProductStock")
    public ResponseEntity<Void> removeProductStock(ProductDTO product) {
        productStockService.deleteProductStock(product);
        return ResponseEntity.ok().build();
    }
}
















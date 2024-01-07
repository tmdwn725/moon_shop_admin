package com.shop.repository.custom;

import com.shop.domain.ProductStock;

public interface ProductStockConfig {
    /**
     * 상품 재고 조회
     * @param productSeq
     * @param sizeType
     * @return
     */
    ProductStock selectProductStock(Long productSeq, String sizeType);
    /**
     * 상품 재고 수정
     * @param productSeq
     * @param size
     * @param count
     */
    void updateProductStockCount(Long productSeq, String size, int count);
}

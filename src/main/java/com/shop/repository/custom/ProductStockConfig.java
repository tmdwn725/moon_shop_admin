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
}

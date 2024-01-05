package com.shop.service;

import com.shop.domain.ProductStock;
import com.shop.dto.ProductDTO;
import com.shop.repository.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductStockService {
    private final ProductStockRepository productStockRepository;
    /**
     * 상품 재고 삭제
     * @param product
     */
    public void deleteProductStock(ProductDTO product){
        ProductStock ps = productStockRepository.selectProductStock(product.getProductSeq(), product.getSizeType());
        productStockRepository.delete(ps);
    }
}

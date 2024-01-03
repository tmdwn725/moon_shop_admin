package com.shop.repository.custom;

import com.shop.domain.Product;
import com.shop.dto.enums.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductConfig {
    Page<Product> selectProductPage(Pageable pageable, ProductType productType, String searchStr);
    Product selectProduct(Long productSeq);
    void updateProductInfo(Product product);
}

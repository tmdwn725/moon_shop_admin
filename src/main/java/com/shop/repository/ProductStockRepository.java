package com.shop.repository;

import com.shop.domain.ProductStock;
import com.shop.repository.custom.ProductStockConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStockRepository extends JpaRepository<ProductStock, Long>, ProductStockConfig {
}

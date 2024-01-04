package com.shop.repository;

import com.shop.domain.ProductFile;
import com.shop.repository.custom.ProductFileConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductFileRepository extends JpaRepository<ProductFile, Long>, ProductFileConfig {
}

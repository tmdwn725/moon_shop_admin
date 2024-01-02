package com.shop.service;

import com.shop.common.FileUtil;
import com.shop.common.ModelMapperUtil;
import com.shop.domain.*;
import com.shop.dto.ProductDTO;
import com.shop.dto.enums.ProductType;
import com.shop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    @Value("${root.filePath}")
    private String filePath;
    @Value("${image.product.path}")
    private String imageUploadPath;
    private final ProductRepository productRepository;
    private final ProductStockRepository productStockRepository;
    private final ProductFileRepository productFileRepository;
    private final FileRepository fileRepository;
    /**
     * 상품목록조회
     * @param start
     * @param limit
     * @return
     */
    public Page<ProductDTO> selectProductList(int start, int limit, ProductType productType, String searchStr){
        PageRequest pageRequest = PageRequest.of(start-1, limit);
        Page<Product> result = productRepository.selectProductPage(pageRequest, productType,searchStr);
        int total = result.getTotalPages();
        pageRequest = PageRequest.of((total-1), limit);
        List<ProductDTO> list = ModelMapperUtil.mapAll(result.getContent(), ProductDTO.class);
        return new PageImpl<>(list, pageRequest, total);
    }
}

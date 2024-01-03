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

    /**
     * 상품 정보 상세 조회
     * @param productSeq
     * @return
     */
    public ProductDTO selectProductInfo(Long productSeq){
        Product productInfo = productRepository.selectProduct(productSeq);
        ProductDTO product = ModelMapperUtil.map(productInfo, ProductDTO.class);
        return product;
    }

    /**
     * 상품정보 저장
     * @param productDTO
     * @param imageFileList
     */
    @Transactional
    public void saveProductInfo(ProductDTO productDTO, MultipartFile[] imageFileList){
        // 현재 날짜와 시간 취득
        LocalDateTime nowDate = LocalDateTime.now();
        Product product = new Product();
        product.createProduct(productDTO.getProductSeq(), productDTO.getSellerSeq(), productDTO.getProductName(), productDTO.getProductContent(), productDTO.getProductType(), productDTO.getPrice(),nowDate, null);

        // 상품정보 수정
        if(productDTO.getProductSeq() > 0){

        }else{ // 상품정보 등록
            productRepository.save(product);
            List<ProductStock> productStockList = productDTO.getSizeTypes().entrySet().stream()
                    .map(entry -> {
                        String sizeType = entry.getKey();
                        String quantity = entry.getValue();
                        ProductStock ps = new ProductStock();
                        ps.createProductStock(product, sizeType, Integer.parseInt(quantity));
                        return ps;
                    })
                    .collect(Collectors.toList());
            productStockRepository.saveAll(productStockList);

            // 상품 이미지 사진 저장
            Arrays.stream(imageFileList)
                    .filter(imageFile -> imageFile.getSize() > 0)
                    .forEach(imageFile -> {

                        String filePth = imageUploadPath + "/" + product.getProductSeq();

                        String saveFilePth = FileUtil.saveFile(imageFile, filePath, filePth);
                        File fileInfo = new File();
                        fileInfo.CreateFile(imageFile.getSize(), nowDate, null, imageFile.getOriginalFilename(), saveFilePth, "jpg");
                        fileRepository.save(fileInfo);

                        String repYn = "N";
                        // index 구하기
                        int index = Arrays.asList(imageFileList).indexOf(imageFile);
                        if (index == 0) {
                            repYn = "Y";
                        }
                        ProductFile productFile = new ProductFile();
                        productFile.createProductFile(product, repYn, fileInfo, index+1);
                        productFileRepository.save(productFile);
                });
        }
    }
}

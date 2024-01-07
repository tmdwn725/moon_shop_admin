package com.shop.service;

import com.shop.common.FileUtil;
import com.shop.common.ModelMapperUtil;
import com.shop.domain.File;
import com.shop.domain.Product;
import com.shop.domain.ProductFile;
import com.shop.domain.ProductStock;
import com.shop.dto.ProductDTO;
import com.shop.dto.enums.ProductType;
import com.shop.repository.FileRepository;
import com.shop.repository.ProductFileRepository;
import com.shop.repository.ProductRepository;
import com.shop.repository.ProductStockRepository;
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
import java.util.stream.Collectors;

@Service
@Transactional
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
    public void saveProductInfo(ProductDTO productDTO, MultipartFile[] imageFileList){
        // 현재 날짜와 시간 취득
        LocalDateTime nowDate = LocalDateTime.now();
        Product product = new Product();
        product.createProduct(productDTO.getProductSeq(), productDTO.getSellerSeq(), productDTO.getProductName(), productDTO.getProductContent(), productDTO.getProductType(), productDTO.getPrice(),nowDate, null);

        // 상품정보 수정
        if(productDTO.getProductSeq() > 0){
            productRepository.updateProductInfo(product);
            for (Map.Entry<String, String> entry : productDTO.getSizeTypes().entrySet()) {
                ProductStock productStock = productStockRepository.selectProductStock(product.getProductSeq(),entry.getKey());
                if(productStock != null){
                    productStockRepository.updateProductStockCount(product.getProductSeq(),entry.getKey(),Integer.parseInt(entry.getValue()));
                }else{
                    ProductStock ps = new ProductStock();
                    ps.createProductStock(product, entry.getKey(), Integer.parseInt(entry.getValue()));
                    productStockRepository.save(ps);
                }
            }

            Arrays.stream(imageFileList)
                    .filter(imageFile -> imageFile.getSize() > 0)
                    .forEach(imageFile -> {
                        String filePth = imageUploadPath + "/" + product.getProductSeq();
                        String repYn = "N";

                        // index 구하기
                        int index = Arrays.asList(imageFileList).indexOf(imageFile);
                        if (index == 0) {
                            repYn = "Y";
                        }
                        // 기준 상품 파일 삭제
                        productFileRepository.deleteProductFile(product.getProductSeq(),repYn,index+1);
                        String saveFilePth = FileUtil.saveFile(imageFile, filePath, filePth);
                        File fileInfo = new File();
                        fileInfo.CreateFile(imageFile.getSize(), nowDate, null, imageFile.getOriginalFilename(), saveFilePth, "jpg");
                        fileRepository.save(fileInfo);

                        ProductFile productFile = new ProductFile();
                        productFile.createProductFile(product, repYn, fileInfo, index+1);
                        productFileRepository.save(productFile);
                    });
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

    /**
     * 상품정보 삭제
     * @param productSeq
     */
    public void removeProduct(Long productSeq){
        Product product = productRepository.findById(productSeq).get();
        List<ProductStock> productStockList = product.getProductStockList();
        fileRepository.delete(product.getProductFileList().get(0).getFile());
        productFileRepository.deleteAll(product.getProductFileList());
        productStockRepository.deleteAll(productStockList);
        productRepository.delete(product);
    }
}

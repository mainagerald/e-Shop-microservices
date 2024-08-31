package com.MyServices.ProductService.service;


import com.MyServices.ProductService.DTO.ProductRequest;
import com.MyServices.ProductService.DTO.ProductResponse;
import com.MyServices.ProductService.model.Product;
import com.MyServices.ProductService.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest){
        Product product=Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        Product savedProduct=productRepository.save(product);
        log.info("Product {} is saved", savedProduct.getId());
        return mapToProductResponse(savedProduct);
    }
    public List<ProductResponse> getAllProducts() {
        List<Product> products= productRepository.findAll();
        System.out.println("Number of products in database: " + products.size());
        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}

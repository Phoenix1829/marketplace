package ru.phoenix.product.service;

import ru.phoenix.product.dto.CreateProductRequest;
import ru.phoenix.product.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProductService {

    ProductResponse createProduct(CreateProductRequest request);

    Page<ProductResponse> getAllProducts(Pageable pageable);

    void deleteProduct(Long productId);
}
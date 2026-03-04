package ru.phoenix.product.controller;

import jakarta.validation.Valid;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.phoenix.product.dto.CreateProductRequest;
import ru.phoenix.product.dto.ProductResponse;
import ru.phoenix.product.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ProductResponse createProduct(
            @Valid @RequestBody CreateProductRequest request) {
        return productService.createProduct(request);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @GetMapping
    public Page<ProductResponse> getAllProducts(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        return productService.getAllProducts(pageable);
    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
}
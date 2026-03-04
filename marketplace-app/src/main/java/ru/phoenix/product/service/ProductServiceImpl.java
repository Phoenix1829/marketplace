package ru.phoenix.product.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.phoenix.exception.AccessDeniedException;
import ru.phoenix.exception.ResourceNotFoundException;
import ru.phoenix.product.dto.CreateProductRequest;
import ru.phoenix.product.dto.ProductResponse;
import ru.phoenix.product.entity.Product;
import ru.phoenix.product.repository.ProductRepository;
import ru.phoenix.security.permission.PermissionService;
import ru.phoenix.user.entity.User;
import ru.phoenix.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PermissionService permissionService;

    public ProductServiceImpl(ProductRepository productRepository,
                              UserRepository userRepository, PermissionService permissionService) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.permissionService = permissionService;
    }

    @CacheEvict(value = "products", allEntries = true)
    @Override
    public ProductResponse createProduct(CreateProductRequest request) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = new Product(
                request.getTitle(),
                request.getDescription(),
                request.getPrice(),
                owner
        );

        Product saved = productRepository.save(product);

        return mapToResponse(saved);
    }

    @CacheEvict(value = {"products", "product"}, allEntries = true)
    @Override
    public void deleteProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        String currentEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        if (!permissionService.canDeleteProduct(
                currentEmail,
                product.getOwner().getEmail())) {

            throw new AccessDeniedException(
                    "You have no permission to delete this product"
            );
        }

        productRepository.delete(product);
    }

    @Cacheable(value = "products",
            key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {

        return productRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Cacheable(value = "product", key = "#id")
    @Override
    public ProductResponse getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        return mapToResponse(product);
    }

    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getOwner().getEmail(),
                product.getCreatedAt()
        );
    }
}
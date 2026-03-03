package ru.phoenix.product.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.phoenix.product.dto.CreateProductRequest;
import ru.phoenix.product.dto.ProductResponse;
import ru.phoenix.product.entity.Product;
import ru.phoenix.product.repository.ProductRepository;
import ru.phoenix.user.entity.User;
import ru.phoenix.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

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

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {

        return productRepository.findAll(pageable)
                .map(this::mapToResponse);
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
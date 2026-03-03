package ru.phoenix.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.phoenix.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
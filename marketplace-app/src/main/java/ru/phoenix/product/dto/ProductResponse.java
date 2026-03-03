package ru.phoenix.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductResponse {

    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private String ownerEmail;
    private LocalDateTime createdAt;

    public ProductResponse(Long id,
                           String title,
                           String description,
                           BigDecimal price,
                           String ownerEmail,
                           LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.ownerEmail = ownerEmail;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public String getOwnerEmail() { return ownerEmail; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
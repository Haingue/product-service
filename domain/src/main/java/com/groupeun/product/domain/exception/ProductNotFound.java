package com.groupeun.product.domain.exception;

import java.util.UUID;

public class ProductNotFound extends DomainException {
    public ProductNotFound() {
        super("Product not found");
    }

    public ProductNotFound(UUID id) {
        super(String.format("Product[id=%s] not found", id));
    }
}

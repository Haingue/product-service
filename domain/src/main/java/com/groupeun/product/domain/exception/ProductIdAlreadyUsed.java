package com.groupeun.product.domain.exception;

import java.util.UUID;

public class ProductIdAlreadyUsed extends DomainException {
    public ProductIdAlreadyUsed() {
        super("Product id already used");
    }

    public ProductIdAlreadyUsed(UUID id) {
        super(String.format("Product[id=%s] already used", id));
    }
}

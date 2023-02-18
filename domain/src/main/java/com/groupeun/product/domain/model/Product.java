package com.groupeun.product.domain.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class Product {

    private UUID id;
    private String name;
    private String description;
    private double nutritionalScore;
    private ProductType type;
    private ProductCategory category;
    private Set<Allergen> allergens = new HashSet<>();
}

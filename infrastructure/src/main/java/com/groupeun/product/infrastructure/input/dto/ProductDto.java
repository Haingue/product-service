package com.groupeun.product.infrastructure.input.dto;

import com.groupeun.product.domain.model.Allergen;
import com.groupeun.product.domain.model.ProductCategory;
import com.groupeun.product.domain.model.ProductType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ProductDto implements Serializable {

    private UUID id;
    private String name;
    private String description;
    private double nutritionalScore;
    private ProductType type;
    private ProductCategory category;
    private Set<Allergen> allergens = new HashSet<>();
}

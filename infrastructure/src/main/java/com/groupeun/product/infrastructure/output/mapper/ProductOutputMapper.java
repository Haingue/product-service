package com.groupeun.product.infrastructure.output.mapper;

import com.groupeun.product.domain.model.Allergen;
import com.groupeun.product.domain.model.Product;
import com.groupeun.product.domain.model.ProductCategory;
import com.groupeun.product.domain.model.ProductType;
import com.groupeun.product.infrastructure.output.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProductOutputMapper {

    public ProductEntity modelToEntity (Product model) {
        ProductEntity entity = new ProductEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        entity.setNutritionalScore(model.getNutritionalScore());
        entity.setType(model.getType().name());
        entity.setCategory(model.getCategory().name());
        entity.getAllergens()
                .addAll(model.getAllergens().stream().map(Allergen::name).collect(Collectors.toList()));
        return entity;
    }

    public Product entityToModel (ProductEntity entity) {
        Product model = new Product();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setNutritionalScore(entity.getNutritionalScore());
        model.setType(ProductType.valueOf(entity.getType()));
        model.setCategory(ProductCategory.valueOf(entity.getCategory()));
        model.getAllergens()
                .addAll(entity.getAllergens().stream().map(Allergen::valueOf).collect(Collectors.toList()));
        return model;
    }
}

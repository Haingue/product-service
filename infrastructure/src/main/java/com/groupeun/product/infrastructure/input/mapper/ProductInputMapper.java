package com.groupeun.product.infrastructure.input.mapper;

import com.groupeun.product.domain.model.Product;
import com.groupeun.product.infrastructure.input.dto.ProductDto;
import org.springframework.stereotype.Component;

@Component
public class ProductInputMapper {

    public ProductDto modelToDto (Product model) {
        ProductDto dto = new ProductDto();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setDescription(model.getDescription());
        dto.setNutritionalScore(model.getNutritionalScore());
        dto.setType(model.getType());
        dto.setCategory(model.getCategory());
        dto.setAllergens(model.getAllergens());
        return dto;
    }

    public Product dtoToModel (ProductDto dto) {
        Product model = new Product();
        model.setId(dto.getId());
        model.setName(dto.getName());
        model.setDescription(dto.getDescription());
        model.setNutritionalScore(dto.getNutritionalScore());
        model.setType(dto.getType());
        model.setCategory(dto.getCategory());
        model.setAllergens(dto.getAllergens());
        return model;
    }
}

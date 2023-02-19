package com.groupeun.product.infrastructure.output.service;

import com.groupeun.product.application.ports.output.ProductOutputPort;
import com.groupeun.product.domain.exception.ProductNotFound;
import com.groupeun.product.domain.model.Allergen;
import com.groupeun.product.domain.model.Product;
import com.groupeun.product.domain.model.ProductCategory;
import com.groupeun.product.domain.model.ProductType;
import com.groupeun.product.infrastructure.output.entity.ProductEntity;
import com.groupeun.product.infrastructure.output.mapper.ProductOutputMapper;
import com.groupeun.product.infrastructure.output.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProductAdapter implements ProductOutputPort {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductOutputMapper productOutputMapper;

    @Override
    public Optional<Product> findOne(UUID id) {
        Optional<ProductEntity> entity = productRepository.findById(id);
        if (entity.isPresent())
            return entity
                .map(productOutputMapper::entityToModel);
        return Optional.empty();
    }

    @Override
    public List<Product> findAll() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .map(productOutputMapper::entityToModel).collect(Collectors.toList());
    }

    @Override
    public List<Product> findAllById(Collection<UUID> idList) {
        return StreamSupport.stream(productRepository.findAllById(idList).spliterator(), false)
                .map(productOutputMapper::entityToModel).collect(Collectors.toList());
    }

    @Override
    public List<Product> findAllByNameRegex(String nameRegex) {
        return productRepository.findAllByNameLikeOrderByName(nameRegex)
                .stream().map(productOutputMapper::entityToModel).collect(Collectors.toList());
    }

    @Override
    public List<Product> search(String nameRegex, ProductType type, ProductCategory category) {
        return productRepository.findAllByNameLikeOrTypeOrCategoryOrderByNameAscTypeAscCategoryAsc(nameRegex, type.name(), category.name())
                .stream().map(productOutputMapper::entityToModel).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<Product> create(UUID id, String name, String description, double nutritionalScore, ProductType type, ProductCategory category, Set<Allergen> allergenSet) {
        ProductEntity entity = new ProductEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setDescription(description);
        entity.setNutritionalScore(nutritionalScore);
        entity.setType(type.name());
        entity.setCategory(category.name());
        allergenSet.stream().map(Allergen::name).forEach(entity.getAllergens()::add);
        entity = productRepository.save(entity);
        return Optional.of(productOutputMapper.entityToModel(entity));
    }

    @Override
    @Transactional
    public Optional<Product> update(UUID id, String name, String description, double nutritionalScore, ProductType type, ProductCategory category, Set<Allergen> allergenSet) {
        ProductEntity existingEntity = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFound(id));
        existingEntity.setName(name);
        existingEntity.setDescription(description);
        existingEntity.setNutritionalScore(nutritionalScore);
        existingEntity.setType(type.name());
        existingEntity.setCategory(category.name());

        existingEntity.getAllergens().retainAll(allergenSet.stream().map(Allergen::name).collect(Collectors.toList()));
        existingEntity = productRepository.save(existingEntity);
        return Optional.of(productOutputMapper.entityToModel(existingEntity));
    }
    @Override
    @Transactional
    public Optional<Product> update(Product model) {
        ProductEntity existingEntity = productOutputMapper.modelToEntity(model);
        existingEntity = productRepository.save(existingEntity);
        return Optional.of(productOutputMapper.entityToModel(existingEntity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        productRepository.deleteById(id);
    }
}

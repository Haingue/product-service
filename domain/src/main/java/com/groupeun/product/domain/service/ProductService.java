package com.groupeun.product.domain.service;

import com.groupeun.product.application.ports.input.ProductInputPort;
import com.groupeun.product.application.ports.output.ProductOutputPort;
import com.groupeun.product.domain.exception.DomainException;
import com.groupeun.product.domain.exception.ProductNotFound;
import com.groupeun.product.domain.model.Allergen;
import com.groupeun.product.domain.model.Product;
import com.groupeun.product.domain.model.ProductCategory;
import com.groupeun.product.domain.model.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ProductService implements ProductInputPort {

    private ProductOutputPort productOutputPort;

    @Override
    public Product findOne(UUID id) {
        return productOutputPort.findOne(id)
                .orElseThrow(() -> new ProductNotFound(id));
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = productOutputPort.findAll();
        if (products.isEmpty()) {
            throw new ProductNotFound();
        }
        return products;
    }

    @Override
    public List<Product> findAllByNameRegex(String nameRegex) {
        List<Product> products = productOutputPort.findAllByNameRegex(nameRegex);
        if (products.isEmpty()) {
            throw new ProductNotFound();
        }
        return products;
    }

    @Override
    public List<Product> search(String nameRegex, ProductType type, ProductCategory category) {
        return productOutputPort.search(nameRegex, type, category);
    }

    @Override
    public Product create(Product product) {
        return this.create(product.getName(), product.getDescription(), product.getNutritionalScore(), product.getType(),
                product.getCategory(), product.getAllergens());
    }

    @Override
    public Product create(String name, String description, double nutritionalScore, ProductType type, ProductCategory category, Set<Allergen> allergenSet) {
        Optional<Product> product = productOutputPort.create(UUID.randomUUID(), name, description, nutritionalScore, type, category, allergenSet);
        return product
                .orElseThrow(() -> new DomainException(String.format("Error to create new Product[name=%s]", name)));
    }

    @Override
    public Product update(Product product) {
        return this.update(product.getId(), product.getName(), product.getDescription(), product.getNutritionalScore(), product.getType(),
                product.getCategory(), product.getAllergens());
    }

    @Override
    public Product update(UUID id, String name, String description, double nutritionalScore, ProductType type, ProductCategory category, Set<Allergen> allergenSet) {
        Product existingProduct = productOutputPort.findOne(id)
                .orElseThrow(() -> new ProductNotFound());
        existingProduct.setName(name);
        existingProduct.setDescription(description);
        existingProduct.setNutritionalScore(nutritionalScore);
        existingProduct.setType(type);
        existingProduct.setCategory(category);
        existingProduct.getAllergens().retainAll(allergenSet);

        Optional<Product> product = productOutputPort.update(existingProduct);
        return product
                .orElseThrow(() -> new DomainException(String.format("Error to update Product[id=%s]", id)));
    }

    @Override
    public void delete(Product product) {
        this.delete(product.getId());
    }

    @Override
    public void delete(UUID id) {
        Product existingProduct = productOutputPort.findOne(id)
                .orElseThrow(() -> new ProductNotFound());
        productOutputPort.delete(existingProduct.getId());
    }
}

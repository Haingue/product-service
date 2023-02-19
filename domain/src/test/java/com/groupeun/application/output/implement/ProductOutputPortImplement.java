package com.groupeun.application.output.implement;

import com.groupeun.product.application.ports.output.ProductOutputPort;
import com.groupeun.product.domain.exception.ProductIdAlreadyUsed;
import com.groupeun.product.domain.exception.ProductNotFound;
import com.groupeun.product.domain.model.Allergen;
import com.groupeun.product.domain.model.Product;
import com.groupeun.product.domain.model.ProductCategory;
import com.groupeun.product.domain.model.ProductType;

import java.util.*;
import java.util.stream.Collectors;

public class ProductOutputPortImplement implements ProductOutputPort {

    private static ProductOutputPortImplement instance = new ProductOutputPortImplement();
    private HashMap<UUID, Product> store;

    private ProductOutputPortImplement () {
        super();
        this.store = new HashMap<>();
    }

    public static ProductOutputPort getInstance () {
        return ProductOutputPortImplement.instance;
    }

    @Override
    public Optional<Product> findOne(UUID id) {
        Product product = store.get(id);
        if (product == null) return Optional.empty();
        return Optional.of(product);
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Product> findAllById(Collection<UUID> idList) {
        return store.entrySet().stream().filter(entry -> idList.contains(entry.getKey()))
                .map(Map.Entry::getValue).collect(Collectors.toList());
    }

    @Override
    public List<Product> findAllByNameRegex(String nameRegex) {
        return store.values().stream()
                .filter(product -> product.getName().matches(nameRegex))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> search(String nameRegex, ProductType type, ProductCategory category) {
        return store.values().stream()
                .filter(product -> product.getName().matches(nameRegex) || product.getType().equals(type) || product.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Product> create(UUID id, String name, String description, double nutritionalScore, ProductType type, ProductCategory category, Set<Allergen> allergenSet) {
        if (store.containsKey(id)) throw new ProductIdAlreadyUsed(id);
        Product newProduct = new Product();
        newProduct.setId(id);
        newProduct.setName(name);
        newProduct.setDescription(description);
        newProduct.setNutritionalScore(nutritionalScore);
        newProduct.setType(type);
        newProduct.setCategory(category);
        newProduct.setAllergens(allergenSet);
        store.put(id, newProduct);
        return Optional.of(newProduct);
    }

    @Override
    public Optional<Product> update(UUID id, String name, String description, double nutritionalScore, ProductType type, ProductCategory category, Set<Allergen> allergenSet) {
        Product existingProduct = store.get(id);
        existingProduct.setName(name);
        existingProduct.setDescription(description);
        existingProduct.setNutritionalScore(nutritionalScore);
        existingProduct.setType(type);
        existingProduct.setCategory(category);
        existingProduct.setAllergens(allergenSet);
        return this.update(existingProduct);
    }

    @Override
    public Optional<Product> update(Product model) {
        if (!store.containsKey(model.getId())) throw new ProductNotFound(model.getId());
        store.put(model.getId(), model);
        return Optional.of(store.get(model.getId()));
    }

    @Override
    public void delete(UUID id) {
        if (!store.containsKey(id)) throw new ProductNotFound(id);
        store.remove(id);
    }
}

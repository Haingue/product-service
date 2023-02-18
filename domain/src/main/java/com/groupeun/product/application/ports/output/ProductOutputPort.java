package com.groupeun.product.application.ports.output;

import com.groupeun.product.domain.model.Allergen;
import com.groupeun.product.domain.model.Product;
import com.groupeun.product.domain.model.ProductCategory;
import com.groupeun.product.domain.model.ProductType;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ProductOutputPort {

    Optional<Product> findOne (UUID id);
    List<Product> findAll ();
    List<Product> findAllByNameRegex (String nameRegex);
    List<Product> search (String nameRegex, ProductType type, ProductCategory category);

    Optional<Product> create (UUID id, String name, String description, double nutritionalScore, ProductType type,
                    ProductCategory category, Set<Allergen> allergenSet);
    Optional<Product> update (UUID id, String name, String description, double nutritionalScore, ProductType type,
                    ProductCategory category, Set<Allergen> allergenSet);
    Optional<Product> update (Product model);

    void delete (UUID id);


}

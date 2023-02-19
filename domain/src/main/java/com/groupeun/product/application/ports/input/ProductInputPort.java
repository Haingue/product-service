package com.groupeun.product.application.ports.input;

import com.groupeun.product.domain.model.Allergen;
import com.groupeun.product.domain.model.Product;
import com.groupeun.product.domain.model.ProductCategory;
import com.groupeun.product.domain.model.ProductType;

import java.util.*;

public interface ProductInputPort {

    Product findOne (UUID id);
    List<Product> findAllByNameRegex (String nameRegex);
    List<Product> findAll ();
    List<Product> findAllById (Collection<UUID> idList);
    List<Product> search (String nameRegex, ProductType type, ProductCategory category);

    Product create (Product product);
    Product create (String name, String description, double nutritionalScore, ProductType type,
                           ProductCategory category, Set<Allergen> allergenSet);
    Product update (Product product);
    Product update (UUID id, String name, String description, double nutritionalScore, ProductType type,
                    ProductCategory category, Set<Allergen> allergenSet);

    void delete (Product product);
    void delete (UUID id);

}

package com.groupeun.domain;

import com.groupeun.application.output.implement.ProductOutputPortImplement;
import com.groupeun.product.application.ports.output.ProductOutputPort;
import com.groupeun.product.domain.exception.ProductIdAlreadyUsed;
import com.groupeun.product.domain.model.Product;
import com.groupeun.product.domain.model.ProductCategory;
import com.groupeun.product.domain.model.ProductType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

public class ProductionServiceTest {

    ProductOutputPort productOutputPort = ProductOutputPortImplement.getInstance();

    @BeforeEach
    public void prepareTest () {
        Assertions.assertDoesNotThrow(() -> {
            productOutputPort.findAll()
                    .forEach(product -> productOutputPort.delete(product.getId()));
        });
    }

    @Test
    public void testInsert () {
        UUID firstId = UUID.randomUUID();
        Assertions.assertDoesNotThrow(() -> {
            productOutputPort.create(firstId, "Test", "Description test", 5D,
                    ProductType.DAIRY_PRODUCTS, ProductCategory.FOOD_AND_GROCERY, new HashSet<>());
        }, "Error to create new Product");
        Assertions.assertDoesNotThrow(() -> {
            Optional<Product> firstProduct = productOutputPort.findOne(firstId);
            Assertions.assertTrue(firstProduct.isPresent());
            Assertions.assertEquals(firstProduct.get().getName(), "Test");
            Assertions.assertEquals(firstProduct.get().getDescription(), "Description test");
            Assertions.assertEquals(firstProduct.get().getNutritionalScore(), 5D);
            Assertions.assertEquals(firstProduct.get().getType(), ProductType.DAIRY_PRODUCTS);
            Assertions.assertEquals(firstProduct.get().getCategory(), ProductCategory.FOOD_AND_GROCERY);
        }, "Error to find product by id");
        Assertions.assertThrows(ProductIdAlreadyUsed.class, () -> {
            productOutputPort.create(firstId, "Test", "Description test", 5D,
                    ProductType.DAIRY_PRODUCTS, ProductCategory.FOOD_AND_GROCERY, new HashSet<>());
        }, "Error to create duplicated Product, ProductIdAlreadyUsed expected");
    }

    @Test
    public void testUpdate () {
        UUID firstId = UUID.randomUUID();
        Assertions.assertDoesNotThrow(() -> {
            productOutputPort.create(firstId, "Test", "Description test", 5D,
                    ProductType.DAIRY_PRODUCTS, ProductCategory.FOOD_AND_GROCERY, new HashSet<>());
        }, "Error to create new Product");
        Assertions.assertDoesNotThrow(() -> {
            productOutputPort.update(firstId, "Test 2", null, 0D, ProductType.BEVERAGES,
                    ProductCategory.FOOD_AND_GROCERY, new HashSet());
            Optional<Product> firstProduct = productOutputPort.findOne(firstId);
            Assertions.assertTrue(firstProduct.isPresent());
            Assertions.assertEquals(firstProduct.get().getName(), "Test 2");
            Assertions.assertNull(firstProduct.get().getDescription());
            Assertions.assertEquals(firstProduct.get().getNutritionalScore(), 0D);
            Assertions.assertEquals(firstProduct.get().getType(), ProductType.BEVERAGES);
            Assertions.assertEquals(firstProduct.get().getCategory(), ProductCategory.FOOD_AND_GROCERY);
            Assertions.assertTrue(firstProduct.get().getAllergens().isEmpty());
        }, "Error to update product");
    }

    @Test
    public void testDelete () {
        UUID firstId = UUID.randomUUID();
        Assertions.assertDoesNotThrow(() -> {
            productOutputPort.create(firstId, "Test", "Description test", 5D,
                    ProductType.DAIRY_PRODUCTS, ProductCategory.FOOD_AND_GROCERY, new HashSet<>());
        }, "Error to create new Product");
        Assertions.assertDoesNotThrow(() -> {
            productOutputPort.delete(firstId);
            Optional<Product> firstProduct = productOutputPort.findOne(firstId);
            Assertions.assertFalse(firstProduct.isPresent());
        }, "Error to delete product by id");
    }
}

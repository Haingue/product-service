package com.groupeun.product.infrastructure.input.controller;

import com.groupeun.product.application.ports.input.ProductInputPort;
import com.groupeun.product.domain.model.Product;
import com.groupeun.product.infrastructure.input.dto.ProductDto;
import com.groupeun.product.infrastructure.input.mapper.ProductInputMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/services/product")
public class ProductController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProductInputPort productInputPort;
    @Autowired
    private ProductInputMapper productInputMapper;

    @GetMapping
    public ResponseEntity<?> getProduct (@PathParam("productId") String productId, @PathParam("nameLike") String nameLike) {
        if (productId != null) {
            logger.info("Load one product: {}", productId);
            Product product = productInputPort.findOne(UUID.fromString(productId));
            return ResponseEntity.ok(productInputMapper.modelToDto(product));
        } else if (nameLike != null) {
            logger.info("Load all product with name like: {}", nameLike);
            nameLike = nameLike.replace('*', '%').replace('.', '?');
            List<Product> products = productInputPort.findAllByNameRegex(nameLike);
            if (products.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(productInputPort.findAll().stream()
                    .map(productInputMapper::modelToDto).collect(Collectors.toList()));
        } else {
            logger.info("Load all product");
            List<Product> products = productInputPort.findAll();
            if (products.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(products.stream()
                    .map(productInputMapper::modelToDto).collect(Collectors.toList()));
        }
    }

    @PostMapping("/details")
    public ResponseEntity<List<ProductDto>> getProductDetails (@RequestBody List<String> idList, Authentication authentication) {
        logger.info("Load product bi id list: {}", idList);
        List<Product> products = productInputPort.findAllById(idList.stream().map(UUID::fromString).collect(Collectors.toList()));
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productInputPort.findAll().stream()
                .map(productInputMapper::modelToDto).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<ProductDto> addProduct (@RequestBody ProductDto productDto, Authentication authentication) {
        logger.info("Add new product {}: {}", authentication.getName(), productDto.getName());
        Product product = productInputPort.create(productInputMapper.dtoToModel(productDto));
        return new ResponseEntity(productInputMapper.modelToDto(product), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ProductDto> updateProduct (@RequestBody ProductDto productDto, Authentication authentication) {
        logger.info("Update product by {}: {}", authentication.getName(), productDto.getId());
        Product product = productInputPort.update(productInputMapper.dtoToModel(productDto));
        return new ResponseEntity(productInputMapper.modelToDto(product), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity deleteProductById (@RequestBody ProductDto productDto, @RequestParam UUID id, Authentication authentication) {
        if (id == null) {
            logger.info("Delete product: {}", productDto.getId());
            productInputPort.delete(productDto.getId());
        } else {
            logger.info("Delete product: {}", id);
            productInputPort.delete(id);
        }
        return ResponseEntity.ok().build();
    }
}

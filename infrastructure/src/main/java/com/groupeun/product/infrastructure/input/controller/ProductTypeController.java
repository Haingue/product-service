package com.groupeun.product.infrastructure.input.controller;

import com.groupeun.product.domain.model.ProductType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/services/type")
public class ProductTypeController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(path = "/all")
    public ResponseEntity<ProductType[]> getAllType (HttpServletResponse response) {
        logger.info("Load all type");
        return ResponseEntity.ok(ProductType.values());
    }
}

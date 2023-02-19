package com.groupeun.product.infrastructure.configuration;

import com.groupeun.product.application.ports.input.ProductInputPort;
import com.groupeun.product.application.ports.output.ProductOutputPort;
import com.groupeun.product.domain.service.ProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfiguration {

    @Bean
    public ProductInputPort productInputPort (ProductOutputPort productOutputPort) {
        return new ProductService(productOutputPort);
    }

}

package com.groupeun.product.infrastructure.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupeun.product.domain.model.Allergen;
import com.groupeun.product.domain.model.ProductCategory;
import com.groupeun.product.domain.model.ProductType;
import com.groupeun.product.infrastructure.input.dto.ProductDto;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.annotation.PostConstruct;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductControllerTest {

    private String endpoint = "/services/product";
    private String token;

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    private void initialize () {
        token = AuthenticationHelper.getAuthentificationToken();
    }

    @Test
    @Order(1)
    void testContextLoading () throws Exception {
        assertThat(mvc).isNotNull();
        assertThat(objectMapper).isNotNull();
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }

    @Test
    @Order(2)
    void testGetAllProduct () throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(endpoint))
                .andExpect(status().isNoContent());
    }


    @Test
    @Order(3)
    void testNewRecipe () throws Exception {
        ProductDto dto = new ProductDto();
        dto.setName("Product 1");
        dto.setDescription("Description test");
        dto.setNutritionalScore(10D);
        dto.setType(ProductType.ETHNIC_FOODS);
        dto.setCategory(ProductCategory.AUTOMOTIVE);
        dto.getAllergens().add(Allergen.CELERY);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(endpoint)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", CoreMatchers.is(dto.getName())))
                .andExpect(jsonPath("$.description", CoreMatchers.is(dto.getDescription())))
                .andExpect(jsonPath("$.nutritionalScore", CoreMatchers.is(dto.getNutritionalScore())))
                .andReturn();
    }
}

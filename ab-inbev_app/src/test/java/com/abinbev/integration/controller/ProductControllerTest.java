package com.abinbev.integration.controller;

import com.abinbev.base.BaseMockMvcTest;
import com.abinbev.controllers.ProductController;
import com.abinbev.exceptions.RecordNotFoundException;
import com.abinbev.integration.service.ProductServiceTest;
import com.abinbev.model.Product;
import com.abinbev.services.ProductServiceImpl;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static com.abinbev.integration.service.ProductServiceTest.assertProductFields;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class ProductControllerTest extends BaseMockMvcTest {
    @Test
    public void createProduct() throws Exception {
        final Product product = ProductServiceTest.newProduct();
        this.mockMvc.perform(post(ProductController.URI_PREFIX)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated());
    }

    @Test
    @DatabaseSetup("classpath:product/products.xml")
    public void deleteProduct() throws Exception {
        final UUID existingId = UUID.fromString("892c40ec-bc7b-497a-bb83-51a499e5baa0");
        this.mockMvc.perform(delete(String.format("%s/%s", ProductController.URI_PREFIX, existingId.toString())))
                .andExpect(status().isOk());
        RecordNotFoundException recordNotFoundException = assertThrows(RecordNotFoundException.class, () -> {
            this.productService.findById(existingId);
        });

        final String validationMessage = this.messageSourceBinder.getMessage(ProductServiceImpl.MessageConstants.PRODUCT_NOT_FOUND, existingId.toString());
        assertThat(recordNotFoundException.getMessage()).isEqualTo(validationMessage);
    }

    @Test
    @DatabaseSetup("classpath:product/products.xml")
    public void updateProduct() throws Exception {
        final UUID existingId = UUID.fromString("892c40ec-bc7b-497a-bb83-51a499e5baa0");
        final Product existingProduct = this.productService.findById(existingId);
        existingProduct.setName("Nova Brahma");
        this.mockMvc.perform(put(String.format("%s/%s", ProductController.URI_PREFIX, existingId.toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(existingProduct)))
                .andExpect(status().isOk());
        final Product updatedProduct = this.productService.findById(existingId);
        assertProductFields(existingProduct, updatedProduct);
    }

    @Test
    @DatabaseSetup("classpath:product/products.xml")
    public void findByName() throws Exception {
        final String name = "antarctica";
        this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/name/%s?page=0&size=10&sort=name,asc", ProductController.URI_PREFIX, name)))
                .andExpect(status().isOk())
                .andExpect(mvcResult -> {
                    assertThat(mvcResult.getResponse().getContentAsString()).isNotBlank();
                    log.info("response: {}", mvcResult.getResponse().getContentAsString());
                })
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Antarctica"))
                .andExpect(jsonPath("$.content[0].brand").value("Antarctica"))
                .andExpect(jsonPath("$.content[0].description").value("traditional brazilian beer"))
                .andExpect(jsonPath("$.content[0].price").value(BigDecimal.valueOf(8.44)));
    }

    @Test
    @DatabaseSetup("classpath:product/products.xml")
    public void findById() throws Exception {
        final UUID productId = UUID.fromString("dab08057-c29d-42a5-b087-164a1e260635");
        final Product existingProduct = this.productService.findById(productId);
        this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/%s", ProductController.URI_PREFIX, existingProduct.getId())))
                .andExpect(status().isOk())
                .andExpect(mvcResult -> {
                    assertThat(mvcResult.getResponse().getContentAsString()).isNotBlank();
                    final Product responseProduct = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Product.class);
                    assertProductFields(existingProduct, responseProduct);
                });
    }

    @Test
    @DatabaseSetup("classpath:product/products.xml")
    public void findAll_sortByNameAsc() throws Exception {
        this.mockMvc.perform(get(String.format("%s?page=0&size=10&sort=name,asc", ProductController.URI_PREFIX)))
                .andExpect(status().isOk())
                .andExpect(mvcResult -> {
                    assertThat(mvcResult.getResponse().getContentAsString()).isNotBlank();
                })
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Antarctica"))
                .andExpect(jsonPath("$.content[1].name").value("Brahma"));
    }

    @Test
    @DatabaseSetup("classpath:product/products.xml")
    public void findAll_sortByNameDesc() throws Exception {
        this.mockMvc.perform(get(String.format("%s?page=0&size=10&sort=name,desc", ProductController.URI_PREFIX)))
                .andExpect(status().isOk())
                .andExpect(mvcResult -> {
                    assertThat(mvcResult.getResponse().getContentAsString()).isNotBlank();
                })
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Brahma"))
                .andExpect(jsonPath("$.content[1].name").value("Antarctica"));
    }

    @Test
    @DatabaseSetup("classpath:product/products.xml")
    public void findById_shouldThrowException() throws Exception {
        final UUID productId = UUID.randomUUID();
        this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/%s", ProductController.URI_PREFIX, productId.toString())))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> {
                    assertThat(mvcResult.getResponse().getContentAsString()).isNotBlank();
                    log.info("response: {}", mvcResult.getResponse().getContentAsString());
                });
    }

    @Test
    public void createProduct_shouldThrowException() throws Exception {
        final Product product = new Product();
        this.mockMvc.perform(post(ProductController.URI_PREFIX)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(product)))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> {
                    assertThat(mvcResult.getResponse().getContentAsString()).isNotBlank();
                    log.info("response: {}", mvcResult.getResponse().getContentAsString());
                });
    }
}

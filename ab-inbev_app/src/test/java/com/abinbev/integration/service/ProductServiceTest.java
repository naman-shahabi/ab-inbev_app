package com.abinbev.integration.service;

import com.abinbev.exceptions.RecordNotFoundException;
import com.abinbev.base.BaseIntegrationTest;
import com.abinbev.model.Product;
import com.abinbev.services.ProductServiceImpl;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductServiceTest extends BaseIntegrationTest {

    @Test
    public void createProduct(){
        final Product product = newProduct();
        this.productService.create(product);
        final Page<Product> productPage = this.productService.findAll(Pageable.unpaged());
        assertThat(productPage.getTotalElements()).isEqualTo(1);
        final List<Product> content = productPage.getContent();
        final Product createdProduct = content.get(0);
        assertProductFields(product, createdProduct);
    }

    @Test
    @DatabaseSetup("classpath:product/products.xml")
    public void updateProduct(){
        final UUID existingProductId = UUID.fromString("892c40ec-bc7b-497a-bb83-51a499e5baa0");
        final Product existingProduct = this.productService.findById(existingProductId);
        existingProduct.setBrand("Antarctica");
        existingProduct.setDescription("Delicious beer");
        existingProduct.setName("Antarctica");
        existingProduct.setPrice(BigDecimal.valueOf(3.45));
        this.productService.save(existingProductId, existingProduct);
        final Product updatedProduct = this.productService.findById(existingProductId);
        assertProductFields(existingProduct, updatedProduct);
    }

    @Test
    @DatabaseSetup("classpath:product/products.xml")
    public void findProduct(){
        final UUID existingProductId = UUID.fromString("892c40ec-bc7b-497a-bb83-51a499e5baa0");
        final Product existingProduct = this.productService.findById(existingProductId);
        assertThat(existingProduct.getName()).isEqualTo("Brahma");
        assertThat(existingProduct.getDescription()).isEqualTo("One of the oldest brazilian beers");
        assertThat(existingProduct.getBrand()).isEqualTo("Brahma Chopp");
        assertThat(existingProduct.getPrice()).isEqualTo(BigDecimal.valueOf(9.76));
    }

    @Test
    @DatabaseSetup("classpath:product/products.xml")
    public void findProduct_shouldThrowException(){
        final UUID randomUUID = UUID.randomUUID();
        RecordNotFoundException recordNotFoundException = assertThrows(RecordNotFoundException.class, () -> {
            this.productService.findById(randomUUID);
        });

        final String validationMessage = this.messageSourceBinder.getMessage(ProductServiceImpl.MessageConstants.PRODUCT_NOT_FOUND, randomUUID.toString());
        Assertions.assertThat(recordNotFoundException.getMessage()).isEqualTo(validationMessage);
    }

    @Test
    @DatabaseSetup("classpath:product/products.xml")
    public void deleteProduct(){
        final UUID existingProductId = UUID.fromString("892c40ec-bc7b-497a-bb83-51a499e5baa0");
        final Product existingProduct = this.productService.findById(existingProductId);
        this.productService.delete(existingProduct.getId());

        RecordNotFoundException recordNotFoundException = assertThrows(RecordNotFoundException.class, () -> {
            this.productService.findById(existingProductId);
        });

        final String validationMessage = this.messageSourceBinder.getMessage(ProductServiceImpl.MessageConstants.PRODUCT_NOT_FOUND, existingProductId.toString());
        Assertions.assertThat(recordNotFoundException.getMessage()).isEqualTo(validationMessage);
    }

    @Test
    @DatabaseSetup("classpath:product/products.xml")
    public void findByName(){
        final String productName = "antarctica";
        final UUID existingId = UUID.fromString("dab08057-c29d-42a5-b087-164a1e260635");
        final Product existingProduct = this.productService.findById(existingId);
        final Page<Product> page = this.productService.findByName(productName, Pageable.unpaged());
        final List<Product> products = page.getContent();
        assertThat(products).isNotEmpty();
        assertThat(products.size()).isEqualTo(1);
        final Product byNameProduct = products.get(0);
        assertProductFields(existingProduct, byNameProduct);
    }

    @Test
    @DatabaseSetup("classpath:product/products.xml")
    public void findAllProducts_sortByNameDesc(){
        final PageRequest sortByNameDesc = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.DESC, "name")));
        final Page<Product> productPage = this.productService.findAll(sortByNameDesc);
        assertThat(productPage.getTotalElements()).isEqualTo(2);
        final List<Product> content = productPage.getContent();
        assertThat(content.get(0).getName()).isEqualTo("Brahma");
        assertThat(content.get(1).getName()).isEqualTo("Antarctica");
    }

    @Test
    @DatabaseSetup("classpath:product/products.xml")
    public void findAllProducts_sortByNameAsc(){
        final PageRequest sortByNameDesc = PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, "name")));
        final Page<Product> productPage = this.productService.findAll(sortByNameDesc);
        assertThat(productPage.getTotalElements()).isEqualTo(2);
        final List<Product> content = productPage.getContent();
        assertThat(content.get(0).getName()).isEqualTo("Antarctica");
        assertThat(content.get(1).getName()).isEqualTo("Brahma");
    }

    public static void assertProductFields(final Product source, final Product target){
        assertThat(source.getName()).isEqualTo(target.getName());
        assertThat(source.getBrand()).isEqualTo(target.getBrand());
        assertThat(source.getDescription()).isEqualTo(target.getDescription());
        assertThat(source.getPrice()).isEqualTo(target.getPrice());
    }

    public static Product newProduct(){
        final Product product = new Product();
        product.setName("Brahma");
        product.setDescription("One of the oldest brazilian beer");
        product.setBrand("Brahma Chopp");
        product.setPrice(BigDecimal.valueOf(10.75));
        return product;
    }
}

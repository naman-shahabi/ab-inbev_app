package com.abinbev.services;

import com.abinbev.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {
    /**
     * Create a new product
     * @param product
     */
    void create(Product product);

    /**
     * Find all Products
     * @param page
     * @return
     */
    Page<Product> findAll(Pageable page);

    /**
     * Find product by Id
     * @param existingProductId
     * @return
     */
    Product findById(UUID existingProductId);

    /**
     * Update product
     * @param existingProduct
     */
    void save(UUID productId, Product existingProduct);

    /**
     * Delete product by id
     * @param id
     */
    void delete(UUID id);

    /**
     * Find product by name
     * @param productName
     * @param pageable
     * @return
     */
    Page<Product> findByName(String productName, Pageable pageable);
}

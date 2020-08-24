package com.abinbev.services;

import com.abinbev.exceptions.ExceptionHelper;
import com.abinbev.exceptions.RecordNotFoundException;
import com.abinbev.model.Product;
import com.abinbev.repositories.ProductRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ExceptionHelper exceptionHelper;

    public ProductServiceImpl(ProductRepository productRepository, ExceptionHelper exceptionHelper) {
        this.productRepository = productRepository;
        this.exceptionHelper = exceptionHelper;
    }

    @Transactional
    @Override
    public void create(Product product) {
        this.productRepository.save(product);
    }

    @Override
    public Page<Product> findAll(Pageable page) {
        return this.productRepository.findAll(page);
    }

    @Override
    public Product findById(UUID existingProductId) {
        final Optional<Product> optProduct = this.productRepository.findById(existingProductId);
        if(!optProduct.isPresent()){
            this.exceptionHelper.throwServiceException(RecordNotFoundException.class, MessageConstants.PRODUCT_NOT_FOUND, existingProductId.toString());
        }
        return optProduct.get();
    }

    @Transactional
    @Override
    public void save(UUID productId, Product product) {
        final Product existingProduct = this.findById(productId);
        existingProduct.setName(product.getName());
        existingProduct.setBrand(product.getBrand());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        this.productRepository.save(existingProduct);
    }

    @Transactional
    @Override
    public void delete(UUID productId) {
        final Product existingProduct = this.findById(productId);
        this.productRepository.delete(existingProduct);
    }

    @Override
    public Page<Product> findByName(String productName, Pageable pageable) {
        return this.productRepository.findByNameContainingIgnoreCase(productName, pageable);
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MessageConstants {
        public static final String PRODUCT_NOT_FOUND = "validation.product.notFound";
    }
}

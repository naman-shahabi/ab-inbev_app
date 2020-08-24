package com.abinbev.controllers;

import com.abinbev.model.Product;
import com.abinbev.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping(value = ProductController.URI_PREFIX)
@RestController
public class ProductController {
    public static final String URI_PREFIX = "/api/v1/product";

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void create(@RequestBody final Product product){
        this.productService.create(product);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{productId}")
    public void delete(@PathVariable("productId") final UUID productId){
        this.productService.delete(productId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{productId}")
    public void update(@PathVariable("productId") final UUID productId, @RequestBody final Product product){
        this.productService.save(productId, product);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Page<Product>> findByName(@PathVariable("name") final String name, Pageable pageable){
        return new ResponseEntity<>(this.productService.findByName(name, pageable), HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> findById(@PathVariable("productId") final UUID productId){
        return new ResponseEntity<>(this.productService.findById(productId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Product>> findAll(Pageable pageable){
        return new ResponseEntity<>(this.productService.findAll(pageable), HttpStatus.OK);
    }

}

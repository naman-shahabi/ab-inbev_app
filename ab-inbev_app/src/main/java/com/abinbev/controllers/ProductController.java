package com.abinbev.controllers;

import com.abinbev.model.Product;
import com.abinbev.services.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(value = ProductController.URI_PREFIX)
@RestController
public class ProductController {
    public static final String URI_PREFIX = "/api/v1/product";

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ApiOperation(value = "Creates a new Product")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void create(@ApiParam(value = "Product definition") @RequestBody final Product product){
        this.productService.create(product);
    }

    @ApiOperation(value = "Deletes a Product by Id")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{productId}")
    public void delete(@ApiParam(value="Product Id") @PathVariable("productId") final UUID productId){
        this.productService.delete(productId);
    }

    @ApiOperation(value = "Updates a Product")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{productId}")
    public void update(@ApiParam(value="Product Id") @PathVariable("productId") final UUID productId, @ApiParam(value = "Product definition") @RequestBody final Product product){
        this.productService.save(productId, product);
    }

    @ApiOperation(value = "Find Products by name containing value")
    @GetMapping("/name/{name}")
    public ResponseEntity<Page<Product>> findByName(@ApiParam(value="Name filter") @PathVariable("name") final String name, @ApiParam(value="Page definition") Pageable pageable){
        return new ResponseEntity<>(this.productService.findByName(name, pageable), HttpStatus.OK);
    }

    @ApiOperation(value = "Find Product by Id")
    @GetMapping("/{productId}")
    public ResponseEntity<Product> findById(@ApiParam(value="Product Id") @PathVariable("productId") final UUID productId){
        return new ResponseEntity<>(this.productService.findById(productId), HttpStatus.OK);
    }

    @ApiOperation(value = "Find all products")
    @GetMapping
    public ResponseEntity<Page<Product>> findAll(@ApiParam(value="Page definition") Pageable pageable){
        return new ResponseEntity<>(this.productService.findAll(pageable), HttpStatus.OK);
    }

}

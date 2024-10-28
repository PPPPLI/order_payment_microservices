package com.cloud.spring.controller;

import com.cloud.spring.service.ProductService;
import com.cloud.spring.sharedEntity.Product;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource
    protected ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {

        return ResponseEntity.ok(productService.getAllProducts());
    }
}

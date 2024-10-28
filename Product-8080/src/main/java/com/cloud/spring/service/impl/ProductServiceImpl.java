package com.cloud.spring.service.impl;

import com.cloud.spring.repository.ProductRepository;
import com.cloud.spring.service.ProductService;
import com.cloud.spring.sharedEntity.Product;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    ProductRepository productRepository;

    @Override
    public Product addProduct(Product product) {

        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }
}

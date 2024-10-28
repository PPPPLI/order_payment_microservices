package com.cloud.spring.service;


import com.cloud.spring.sharedEntity.Product;

import java.util.List;

public interface ProductService {

    Product addProduct(Product product);

    List<Product> getAllProducts();
}

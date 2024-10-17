package com.cloud.spring.service.impl;

import com.cloud.spring.Main8084;
import com.cloud.spring.service.ProductService;
import com.cloud.spring.sharedEntity.Product;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = Main8084.class)
class ProductServiceImplTest {

    @Resource
    ProductService productService;

    @Test
    void addProduct() {

        Product product = Product.builder().productName("Football").productPrice(25.0).productQuantity(100).build();

        productService.addProduct(product);
    }
}
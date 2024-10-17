package com.cloud.spring.repository;

import com.cloud.spring.sharedEntity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query("update product set productQuantity = productQuantity- :productQuantity where productName = :productName")
    @Modifying
    void updateProductByProduct_name(@Param("productName") String productName, @Param("productQuantity") Integer productQuantity);
}

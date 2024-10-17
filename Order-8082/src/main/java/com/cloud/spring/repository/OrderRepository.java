package com.cloud.spring.repository;


import com.cloud.spring.sharedEntity.OrderForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderForm, UUID> {

    List<OrderForm> findOrderByOrderDateAndOrderOwnerName(LocalDateTime date, String name);

    List<OrderForm> findOrderByOrderOwnerName(String ownerName);


    OrderForm deleteOrderByOrderId(UUID id);
}

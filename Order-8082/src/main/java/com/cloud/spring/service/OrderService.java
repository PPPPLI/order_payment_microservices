package com.cloud.spring.service;

import com.cloud.spring.sharedEntity.OrderForm;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<OrderForm> findOrderByDateAndCustomer(LocalDateTime date, String customer);

    List<OrderForm> findOrderByCustomer(String customer);

    OrderForm saveOrder(OrderForm order);

    OrderForm deleteOrder(UUID id);

}

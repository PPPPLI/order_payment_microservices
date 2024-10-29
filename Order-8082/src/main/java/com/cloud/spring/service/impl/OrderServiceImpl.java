package com.cloud.spring.service.impl;

import com.cloud.spring.repository.OrderRepository;
import com.cloud.spring.service.OrderService;
import com.cloud.spring.sharedEntity.OrderForm;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderRepository orderRepository;

    /**
     * Get orders by username and specific date. Firstly we will find the relative data in cache memory,
     * If there isn't it, we read from the database. Once we get the data from database, and we write them
     * in the cache memory in order that we can get the same data directly in Redis next time
     * */
    @Override
    @Cacheable(cacheNames = "order-cache",key = "#customer+'-'+#date")
    public List<OrderForm> findOrderByDateAndCustomer(LocalDateTime date, String customer) {

        log.info("Date: {} - Customer:{}", date, customer);

        return orderRepository.findOrderByOrderDateAndOrderOwnerName(date, customer);

    }

    /**
     * Always the same cache storage logic, but here we get orders just by username
     * */
    @Override
    @Cacheable(cacheNames = "order-cache",key = "#customer", condition = "#customer != null and #customer.length() > 0", unless = "#result == null")
    @Transactional(readOnly = true)
    public List<OrderForm> findOrderByCustomer(String customer) {

        log.error("Service Parameter is {}",customer);

        return orderRepository.findOrderByOrderOwnerName(customer);
    }


    /**
     * Write or update the order data
     * */
    @Override
    @CachePut(value ="order-cache", key ="#result.orderOwnerName+'-'+#result.orderDate")
    @CacheEvict(value = "order-cache", key = "#result.orderOwnerName", beforeInvocation = false)
    @Transactional
    public OrderForm saveOrder(OrderForm order) {

        return orderRepository.save(order);

    }


    /**
     * As the name of this method, it's for deleting the order by its id, it also deletes the relative cache data
     * */
    @Override
    @CacheEvict(value = "order-cache",key = "{#result.orderOwnerName,#result.orderOwnerName+'-'+result.orderDate}", beforeInvocation = false)
    @Transactional
    public OrderForm deleteOrder(UUID id) {

        return orderRepository.deleteOrderByOrderId(id);

    }


}

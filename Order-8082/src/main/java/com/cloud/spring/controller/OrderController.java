package com.cloud.spring.controller;

import com.cloud.spring.service.OrderService;
import com.cloud.spring.service.impl.RpcOrderServiceImpl;
import com.cloud.spring.sharedEntity.OrderForm;
import com.cloud.spring.sharedEntity.Product;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.examples.lib.PaymentResultReply;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
public class OrderController {

    @Resource
    private OrderService orderService;

    @Resource
    private RpcOrderServiceImpl rpcOrderService;



    /**
     * @apiNote This is a most delicate api, it will communicate with the two other services to finish payment and
     * update the storage of products. Because of the structure of microservices, we must think about failure case when writing into
     * database. So the SAGA pattern is considered here to process data compensation.
     *
     * @param  order: {orderOwnerName,isPaid,productList} in Json format
     * */
    @PostMapping("/order/payment")
    public ResponseEntity<String> payment(@RequestBody OrderForm order) {

        order.setOrderDate(LocalDateTime.now());
        order.setOrderId(UUID.randomUUID());
        order.getOrderProducts().forEach(ele -> ele.setOrder(order));

        if(orderService.saveOrder(order) == null){

            log.error("{} - Order failed", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Please try later");
        }

        log.info("{} - Order created", LocalDateTime.now());


        //Sum the product's price
        Double price = order.getOrderProducts().stream().map(product -> product.getProduct().getProductPrice() * product.getQuantity()).reduce(0.0, Double::sum);

        //Process the payment
        PaymentResultReply reply = rpcOrderService.OrderToPayment(order.getOrderOwnerName(), price,order.getOrderId().toString());

        //If it failed, return failure response
        if(!reply.getOrderPaid()){

            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Please try later");
        }

        //process to the product storage
        List<Product> productList = new ArrayList<>();

        order.getOrderProducts().forEach(ele -> productList.add(ele.getProduct()));


        //If the product service processes correctly, update status of the relative order(isPaid = true)
        if(rpcOrderService.OrderToProduct(productList).getIsSucceed()){

            order.setPaid(true);
            orderService.saveOrder(order);

            log.info("{} - Order is paid", LocalDateTime.now());


            return ResponseEntity.ok("Payment successfully done");

        }


        //If not, process the payment data compensation request
        rpcOrderService.deletePayment(reply.getPaymentId());
        log.error("{} - Payment failed", LocalDateTime.now());
        //In the end, return a failure response
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Please try later");

    }


    /**
     * @apiNote Order delete api by orderId
     * @param order:{orderId}
     * */
    @DeleteMapping("/order/delete")
    public ResponseEntity<String> deleteOrder(@RequestBody OrderForm order) {

        if(orderService.deleteOrder(order.getOrderId()) == null){

            log.error("{} - order delete failed", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Please try later");

        }

        log.info("{} - Order is deleted successfully", LocalDateTime.now());

        return ResponseEntity.ok("Order deleted successfully");

    }


    /**
     * @apiNote Get all orders by username
     * @param orderOwnerName{userName}
     * */
    @GetMapping("/order/{name}")
    public ResponseEntity<List<OrderForm>> getOrder(@PathVariable("name") String orderOwnerName) {

        log.error("Parameter is {}",orderOwnerName);

        return ResponseEntity.ok(orderService.findOrderByCustomer(orderOwnerName));
    }


    /**
     * @apiNote Get relative orders by username and a specific date
     * @param orderOwnerName{username}
     * @param orderDate{a specific date}
     * */
    @GetMapping("order/get")
    public ResponseEntity<List<OrderForm>> getOrderById(String orderOwnerName, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime orderDate) {

        return ResponseEntity.ok(orderService.findOrderByDateAndCustomer(orderDate,orderOwnerName));
    }
}

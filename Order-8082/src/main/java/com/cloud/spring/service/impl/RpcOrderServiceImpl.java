package com.cloud.spring.service.impl;

import com.cloud.spring.sharedEntity.Product;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.examples.lib.*;
import org.springframework.stereotype.Service;

import java.util.List;

/***
 * Grpc request, service communicate with Payment service and Product Service
 * */
@Service
@Slf4j
public class RpcOrderServiceImpl {

    @GrpcClient("OrderService")
    private OrderServiceGrpc.OrderServiceBlockingStub orderServiceBlockingStub;

    @GrpcClient("ProductOrderService")
    private ProductOrderServiceGrpc.ProductOrderServiceBlockingStub productOrderServiceBlockingStub;


    /**
     * Request to finish the payment
     * @param userName:username
     * @param price:price
     * @param orderId:orderId
     * @return PaymentResultReply:{Success status, PaymentId}
     *
     * */
    public PaymentResultReply OrderToPayment(String userName, double price,String orderId) {

        return orderServiceBlockingStub.sendOrderToPayment(
                OrderPaymentRequest.newBuilder()
                        .setUserName(userName)
                        .setOrderPrice(price)
                        .setOrderId(orderId)
                        .build()
        );
    }

    /***
     * Payment data compensation request in case of failure
     * @param paymentId :paymentId
     * @return PaymentResultReply:{Success status, PaymentId}
     * */
    public PaymentResultReply deletePayment(String paymentId){

        return orderServiceBlockingStub.deletePayment(DeleteRequest.newBuilder().setPaymentId(paymentId).build());
    }


    /***
     * Product grpc request, to update the storage according to order
     * @param productList :all the products in the order
     * @return ProductResultReply:{Processing status}
     * */
    public ProductResultReply OrderToProduct(List<Product> productList) {

        List<net.devh.boot.grpc.examples.lib.Product> list = productList.stream()
                            .map(product-> net.devh.boot.grpc.examples.lib.Product.newBuilder()
                                    .setProductName(product.getProductName())
                                    .setProductQuantity(product.getProductQuantity()).build())
                            .toList();

        OrderProductRequest productRequest = OrderProductRequest.newBuilder().addAllProducts(list).build();

        return productOrderServiceBlockingStub.sendOrderToProduct(productRequest);
    }
}

package com.cloud.spring.service;

import com.cloud.spring.repository.ProductRepository;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.Resource;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.examples.lib.OrderProductRequest;

import net.devh.boot.grpc.examples.lib.Product;
import net.devh.boot.grpc.examples.lib.ProductOrderServiceGrpc;
import net.devh.boot.grpc.examples.lib.ProductResultReply;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/***
 * Product grpc request, service side
 * */
@GrpcService
@Slf4j
public class RpcProductService extends ProductOrderServiceGrpc.ProductOrderServiceImplBase {

    @Resource
    private ProductRepository productRepository;

    /**
     * Update the product storage according to the order
     * */
    @Override
    @Transactional
    public void sendOrderToProduct(OrderProductRequest request, StreamObserver<ProductResultReply> responseObserver) {

        List<Product> list = request.getProductsList();
        try {

            list.forEach(each -> productRepository.updateProductByProduct_name(each.getProductName(),each.getProductQuantity()));
            responseObserver.onNext(ProductResultReply.newBuilder().setIsSucceed(true).build());
            log.info("Successfully sent order to product");
        }catch (Exception e){

            responseObserver.onNext(ProductResultReply.newBuilder().setIsSucceed(false).build());
            log.error("Error while sending order to product", e);

        }finally {
            responseObserver.onCompleted();
        }

    }
}

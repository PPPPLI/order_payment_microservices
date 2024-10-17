package com.cloud.spring.service;

import com.cloud.spring.repository.PaymentRepository;
import com.cloud.spring.sharedEntity.OrderForm;
import com.cloud.spring.vo.Payment;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.Resource;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.examples.lib.DeleteRequest;
import net.devh.boot.grpc.examples.lib.OrderPaymentRequest;
import net.devh.boot.grpc.examples.lib.OrderServiceGrpc;
import net.devh.boot.grpc.examples.lib.PaymentResultReply;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.LocalDateTime;
import java.util.UUID;

/***
 * Payment grpc request service side
 * */
@GrpcService
@Slf4j
public class RpcPaymentServiceImpl extends OrderServiceGrpc.OrderServiceImplBase {

    @Resource
    PaymentRepository paymentRepository;

    /***
     * Save the payment into the database, and return the processing status with payment id
     * */
    @Override
    public void sendOrderToPayment(OrderPaymentRequest request, StreamObserver<PaymentResultReply> responseObserver) {

        Payment payment = Payment.builder()
                .paymentDate(LocalDateTime.now())
                .userName(request.getUserName())
                .order(OrderForm.builder().orderId(UUID.fromString(request.getOrderId())).build())
                .paymentAmount(request.getOrderPrice())
                .build();

        try {
            Payment Savedpayment = paymentRepository.save(payment);
            responseObserver.onNext(PaymentResultReply.newBuilder().setOrderPaid(true).setPaymentId(Savedpayment.getPaymentId().toString()).build());
            log.info("Payment saved successfully");
        }catch (Exception e){

            responseObserver.onNext(PaymentResultReply.newBuilder().setOrderPaid(false).build());
            log.error("Payment save failed");
        }finally {
            responseObserver.onCompleted();
        }

    }

    /**
     * Data compensation request, delete the previous saved payment with its id
     * */
    @Override
    public void deletePayment(DeleteRequest request, StreamObserver<PaymentResultReply> responseObserver) {

        UUID paymentId = UUID.fromString(request.getPaymentId());

        paymentRepository.deleteById(paymentId);

        responseObserver.onNext(PaymentResultReply.newBuilder().setOrderPaid(true).build());
        log.info("Payment deleted successfully");
        responseObserver.onCompleted();
    }
}

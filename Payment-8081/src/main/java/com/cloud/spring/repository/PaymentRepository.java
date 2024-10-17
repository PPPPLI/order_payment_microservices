package com.cloud.spring.repository;

import com.cloud.spring.vo.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {


}

package com.cloud.spring.vo;

import com.cloud.spring.sharedEntity.OrderForm;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID paymentId;
    private String userName;
    private LocalDateTime paymentDate;
    private Double paymentAmount;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "orderId")
    private OrderForm order;

}

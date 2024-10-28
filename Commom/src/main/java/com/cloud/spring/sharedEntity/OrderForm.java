package com.cloud.spring.sharedEntity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Order entity
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "order_form")
public class OrderForm implements Serializable {


    @Id
    private UUID orderId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;
    private String orderOwnerName;
    private boolean isPaid;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("order")
    private List<OrderProduct> orderProducts;

    @Override
    public String toString() {
        return "OrderForm{" +
                "orderId=" + orderId +
                ", orderDate=" + orderDate +
                ", orderOwnerName='" + orderOwnerName + '\'' +
                ", isPaid=" + isPaid +
                '}';
    }
}

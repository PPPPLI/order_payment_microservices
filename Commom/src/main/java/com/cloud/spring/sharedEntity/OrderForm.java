package com.cloud.spring.sharedEntity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;
    private String orderOwnerName;
    private boolean isPaid;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "order_product",
            joinColumns = @JoinColumn(name = "orderId"),
            inverseJoinColumns = @JoinColumn(name = "productId")
    )
    private List<Product> productList;
}

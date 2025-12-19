package com.carolinawings.webapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "order_item")
@Table(name = "order_item_options")
public class OrderItemOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;
    private String groupName;
    private String optionName;
    @Column(precision = 10, scale = 2)
    private BigDecimal extraPrice;
}

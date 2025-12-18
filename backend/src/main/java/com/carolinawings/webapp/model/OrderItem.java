/**
 * @
 * Written by Ty Bennett
 * the purpose of this class is to provide a way for a menu item to be associated within an order without messing with
 * the menu item itself. Relevant fields include the order itself, menuitem id to get its PK identifier
 * prices for the menu items,
 */

package com.carolinawings.webapp.model;

import com.carolinawings.webapp.dto.OrderItemOptionDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import java.util.List;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString(exclude = "order")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    // menu item reference
    private Long menuItemId;
    // items name
    private String menuItemName;
    @Column(precision = 10, scale = 2)
    private BigDecimal menuItemPrice;
    @Min(value = 1)
    private Integer quantity;
    @Column(precision = 10, scale = 2)
    private BigDecimal totalPrice;
    private List<OrderItemOptionDTO> options;


    @PrePersist
    @PreUpdate
    public void prePersist() {
        if(this.menuItemPrice == null) {
            this.menuItemPrice = BigDecimal.ZERO;
        }
        this.totalPrice = this.menuItemPrice.multiply(BigDecimal.valueOf(quantity));
    }
}

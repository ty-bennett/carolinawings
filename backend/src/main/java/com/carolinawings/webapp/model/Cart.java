package com.carolinawings.webapp.model;

// import com.fasterxml.jackson.annotation.JsonIgnore;
import com.carolinawings.webapp.enums.CartStatus;
import jakarta.persistence.*;
import lombok.*;
// import org.springframework.core.annotation.Order;

import java.math.BigDecimal;
// import java.util.ArrayList;
import java.util.HashSet;
//import java.util.List;
import java.util.Set;

@Entity
@Table(name = "carts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "cart", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @OrderBy("id ASC")
    private Set<CartItem> cartItems = new HashSet<>();

    private BigDecimal totalTax;
    private BigDecimal subtotal;
    private BigDecimal totalPrice;
    @Enumerated(EnumType.STRING)
    private CartStatus cartStatus;
}

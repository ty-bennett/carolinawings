package com.carolinawings.webapp.model;

// import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @OneToMany(mappedBy = "cart", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    @OrderBy("id ASC")
    private Set<CartItem> cartItems = new HashSet<>();

    private BigDecimal totalPrice;
}

//decided to handle server side b/c it could be saved across devics and people could come back later to order stuff
 //this could be handled clientside, and porbably will be but I will get to it
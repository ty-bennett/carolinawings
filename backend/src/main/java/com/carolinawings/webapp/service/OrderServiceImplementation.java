/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.OrderCreateRequest;
import com.carolinawings.webapp.dto.OrderDTO;
import com.carolinawings.webapp.dto.OrderResponseDTO;
import com.carolinawings.webapp.enums.OrderStatus;
import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.mapper.OrderMapper;
import com.carolinawings.webapp.model.*;
import com.carolinawings.webapp.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class OrderServiceImplementation implements OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;

    public OrderServiceImplementation(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

//    public OrderResponse getAllOrdersByRestaurantPaged(Integer pageNumber, Integer pageSize, Long restaurantId) {
//        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
//        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new ResourceNotFoundException("Restaurant", "id", restaurantId));
//        Page<Order> orders = orderRepository.findOrdersByRestaurantAssignedTo(restaurantId, pageDetails);
//
//        if (orders.isEmpty())
//            throw new APIException("No orders present");
//        List<OrderDTO> orderDTOS = orders.stream()
//                .map(order -> modelMapper.map(order, OrderDTO.class))
//                .toList();
//
//        OrderResponse oR = new OrderResponse();
//        oR.setContent(orderDTOS);
//        oR.setPageNumber(orders.getNumber());
//        oR.setPageSize(orders.getSize());
//        oR.setTotalElements(orders.getTotalElements());
//        oR.setTotalPages(orders.getTotalPages());
//        oR.setLastPage(orders.isLast());
//        return oR;
//    }
//

    @Override
    public OrderResponseDTO getAllOrdersByRestaurantPaged(Integer pageNumber, Integer pageSize, Long restaurantId) {
        return null;
    }

    @Override
    public Optional<OrderDTO> getOrderById(UUID id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(element -> modelMapper.map(element, OrderDTO.class));
    }

    @Override
    public OrderDTO deleteOrderById(UUID id) {
        Order o = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", id));
        orderRepository.delete(o);
        return modelMapper.map(o, OrderDTO.class);
    }

    @Override
    public OrderDTO updateOrder(OrderDTO orderDTO, UUID id) {
        Order savedOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", id));
        Order order = modelMapper.map(orderDTO, Order.class);
        order.setId(id);
        Order savedOrderToRepo = orderRepository.save(order);
        return modelMapper.map(savedOrderToRepo, OrderDTO.class);
    }

    @Override
    public OrderResponseDTO createOrderFromCart(OrderCreateRequest request) {
        // load the cart
        Cart cart = cartRepository.findCartById(request.getCartId())
                        .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", request.getCartId()));
        // validate the cart has items in it
        Set<CartItem> cartItems = cart.getCartItems();
        if(cartItems == null || cartItems.isEmpty()) {
            throw new APIException("CART IS EMPTY");
        }

        // load the restaurant
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                        .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "restaurantId", request.getRestaurantId()));
        Order order = modelMapper.map(request, Order.class);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.PENDING);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        OffsetDateTime pickupTime;
        if(request.getRequestedPickupTime().isBlank() || request.getRequestedPickupTime() == null) {
            pickupTime = OffsetDateTime.now().plusMinutes(15);
        } else {
            pickupTime = LocalDateTime.parse(request.getRequestedPickupTime(), formatter).atOffset(ZoneOffset.of("-05:00"));
        }
        order.setPickupTime(pickupTime);
        order.setUser(cart.getUser());
        order.setSubtotal(cart.getSubtotal());
        order.setTotalTax(cart.getTotalTax());
        order.setTotalPrice(cart.getTotalPrice());

        List<OrderItem> orderItems = new ArrayList<>();

        for(CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);

            MenuItem menuItem = cartItem.getMenuItem();
            orderItem.setMenuItemId(menuItem.getId());
            orderItem.setMenuItemName(menuItem.getName());
            orderItem.setMenuItemPrice(menuItem.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());

            // total price will be filled by an @PrePersist method


            //TODO: map cart item options -> orderItemOption and attach!
            for(CartItemChoice cartItemChoice : cartItem.getChoices()) {
            }

            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        // order build from cart
        Order saved = orderRepository.save(order);

        return OrderMapper.toOrderResponseDTO(saved);
    }
}


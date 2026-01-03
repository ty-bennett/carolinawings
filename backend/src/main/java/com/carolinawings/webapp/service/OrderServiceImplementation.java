/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.*;
import com.carolinawings.webapp.enums.CartStatus;
import com.carolinawings.webapp.enums.OrderStatus;
import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.mapper.OrderMapper;
import com.carolinawings.webapp.messaging.OrderMessage;
import com.carolinawings.webapp.messaging.OrderProducer;
import com.carolinawings.webapp.model.*;
import com.carolinawings.webapp.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
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

    @Autowired
    private OrderProducer orderProducer;

    public OrderServiceImplementation(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderResponseDTO getAllOrdersByRestaurantPaged(Integer pageNumber, Integer pageSize, Long restaurantId) {
        return null;
    }

    @Override
    public Optional<OrderDTO> getOrderById(UUID id) {
        return orderRepository.findById(id).map(this::mapOrderToDTO);
    }

    private OrderDTO mapOrderToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getId());
        dto.setStatus(order.getStatus().name());
        dto.setSubtotal(order.getSubtotal());
        dto.setTotalTax(order.getTotalTax());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        dto.setPickupTime(order.getPickupTime());
        dto.setCustomerName(order.getCustomerName());
        dto.setCustomerPhone(order.getCustomerPhone());
        dto.setCustomerNotes(order.getCustomerNotes());
        dto.setOrderType(order.getOrderType().name());
        dto.setRestaurantId(order.getRestaurant().getId());
        dto.setRestaurantName(order.getRestaurant().getName());

        if (order.getItems() != null) {
            dto.setItems(order.getItems().stream()
                    .map(this::mapOrderItemToDTO)
                    .toList());
        }

        return dto;
    }

    private OrderItemDTO mapOrderItemToDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());
        dto.setMenuItemId(item.getMenuItemId());
        dto.setMenuItemName(item.getMenuItemName());
        dto.setUnitPrice(item.getMenuItemPrice());
        dto.setQuantity(item.getQuantity());
        dto.setLineTotal(item.getTotalPrice());

        if (item.getOptions() != null) {
            dto.setOptions(item.getOptions().stream()
                    .map(this::mapOrderItemOptionToDTO)
                    .toList());
        }

        return dto;
    }

    private OrderItemOptionDTO mapOrderItemOptionToDTO(OrderItemOption option) {
        OrderItemOptionDTO dto = new OrderItemOptionDTO();
        dto.setOptionId(option.getId());
        dto.setOptionGroupName(option.getGroupName());
        dto.setOptionName(option.getOptionName());
        dto.setExtraPrice(option.getExtraPrice());
        return dto;
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
        if(cart.getCartStatus() != CartStatus.ACTIVE) {
            throw new APIException("CART IS NOT ACTIVE");
        }

        // validate the cart has items in it
        Set<CartItem> cartItems = cart.getCartItems();
        if(cartItems == null || cartItems.isEmpty()) {
            throw new APIException("CART IS EMPTY");
        }

        // load the restaurant
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                        .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "restaurantId", request.getRestaurantId()));
        if(!restaurant.isAcceptingOrders()) {
           throw new APIException("Restaurant IS NOT ACCEPTING ORDERS");
        }
        //get restaurant from cart if possible
        Restaurant restaurantFromCart = getRestaurantFromCart(cart);

        if(restaurantFromCart == null || !restaurantFromCart.getId().equals(restaurant.getId())) {
            throw new APIException("Cart does not belong to that restaurant");
        }


        Order order = new Order();

        order.setCustomerName(request.getCustomerName());
        order.setCustomerPhone(request.getCustomerPhone());
        order.setCustomerNotes(request.getCustomerNotes());

        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.PENDING);

        OffsetDateTime pickupTime;
        String requestedPickupTime = request.getRequestedPickupTime();

        if(requestedPickupTime == null || requestedPickupTime.isBlank()) {
            pickupTime = OffsetDateTime.now().plusMinutes(restaurant.getEstimatedPickupMinutes());
        } else {
            // Parse ISO format directly - no custom formatter needed
            pickupTime = OffsetDateTime.parse(requestedPickupTime);
        }
        order.setPickupTime(pickupTime);

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
            List<OrderItemOption> options = new ArrayList<>();

            for(CartItemOption choice : cartItem.getChoices()) {
                MenuItemOption menuItemOption = choice.getMenuItemOption();

                OrderItemOption orderItemOption = new OrderItemOption();
                orderItemOption.setOrderItem(orderItem);
                orderItemOption.setOptionName(menuItemOption.getName());
                orderItemOption.setExtraPrice(menuItemOption.getPrice().setScale(2, RoundingMode.HALF_UP));
                orderItemOption.setGroupName(choice.getChoiceType());
                options.add(orderItemOption);
            }

            orderItem.setOptions(options);
            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        // order build from cart
        Order saved = orderRepository.save(order);
        cart.setCartStatus(CartStatus.CHECKED_OUT);
        cartRepository.save(cart);

        // try catch in case rabbit mq servicve is down
        try {
            OrderMessage orderMessage = OrderMessage.builder()
                    .orderId(saved.getId())
                    .restaurantId(saved.getRestaurant().getId())
                    .restaurantName(saved.getRestaurant().getName())
                    .customerName(saved.getCustomerName())
                    .customerPhone(saved.getCustomerPhone())
                    .customerNotes(saved.getCustomerNotes())
                    .pickupTime(saved.getPickupTime())
                    .totalPrice(saved.getTotalPrice())
                    .items(saved.getItems().stream()
                            .map(item -> OrderMessage.OrderItemMessage.builder()
                                    .name(item.getMenuItemName())
                                    .quantity(item.getQuantity())
                                    .price(item.getTotalPrice())
                                    .options(item.getOptions().stream()
                                            .map(opt -> opt.getOptionName())
                                            .toList())
                                    .build())
                            .toList())
                    .build();

            orderProducer.sendOrderToKitchen(orderMessage);
        } catch (Exception e) {
            log.error("Failed to send order {} to kitchen queue: {}", saved.getId(), e.getMessage());
            // Order is still saved - kitchen can view it in admin panel
        }        return OrderMapper.toOrderResponseDTO(saved);
    }

    @Override
    public PagedOrderResponseDTO getOrdersForRestaurantByManager(
            Long restaurantId,
            Integer page,
            Integer pageSize,
            OrderStatus statusFilter
    ) {
        User currentUser = getCurrentUser();

        Restaurant restaurant =  restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "restaurantId", restaurantId));

        if(!userManagesRestaurant(currentUser, restaurant)) {
            throw new APIException("User does not have access to manage that restaurant" + restaurantId);
        }


        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Order> orderPage;
        if(statusFilter != null) {
            orderPage = orderRepository.findByRestaurantIdAndStatus(restaurantId, statusFilter, pageable);
        } else {
            orderPage = orderRepository.findByRestaurantId(restaurantId, pageable);
        }

        if(orderPage.isEmpty()) {
            throw new APIException("No orders found for this restaurant" + restaurant.getName());
        }

        List<OrderResponseDTO> dtos = orderPage
                .getContent()
                .stream()
                .map(OrderMapper::toOrderResponseDTO)
                .collect(Collectors.toList());

        return PagedOrderResponseDTO.builder()
                .content(dtos)
                .totalElements(orderPage.getTotalElements())
                .pageNumber(orderPage.getNumber())
                .pageSize(orderPage.getSize())
                .totalPages(orderPage.getTotalPages())
                .lastPage(orderPage.isLast())
                .build();
    }

    @Override
    public OrderResponseDTO updateOrderStatusForManager(UUID orderId, OrderStatus orderStatus) {
        User currentUser = getCurrentUser();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException());

        Restaurant restaurant = order.getRestaurant();
        if(!userManagesRestaurant(currentUser, restaurant)) {
            throw new APIException("User does not have access to manage that restaurant" + restaurant.getName());
        }

        order.setStatus(orderStatus);
        Order saved = orderRepository.save(order);

        return OrderMapper.toOrderResponseDTO(saved);
    }

    @Override
    public OrderDTO cancelOrder(UUID id) {
        Order order =  orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", id));
        OrderStatus orderStatus = order.getStatus();
        if(orderStatus.equals(OrderStatus.PENDING)) {
            order.setStatus(OrderStatus.CANCELLED);
        } else {
            throw new APIException("order cannot be cancelled");
        }
        Order savedOrder = orderRepository.save(order);
        return modelMapper.map(savedOrder, OrderDTO.class);
    }

    @Override
    public Page<OrderDTO> getOrdersForCurrentUser(int page, int pageSize) {
        String username = getCurrentUser().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new APIException("User not found"));

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        Page<Order> orders = orderRepository.findByUser(user, pageable);

        return orders.map(this::convertToDTO);
    }


    // private helper methods
    private boolean userManagesRestaurant(User user, Restaurant restaurant) {
        if(user == null || restaurant == null) return false;

        return user.getRestaurants() != null && user.getRestaurants().contains(restaurant);
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || !auth.isAuthenticated()) return null;

        String username;
        Object principal = auth.getPrincipal();
        if(principal instanceof UserDetails ud) {
            username = ud.getUsername();
        } else {
            username = principal.toString();
        }

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    private Restaurant getRestaurantFromCart(Cart cart) {
        return cart.getCartItems().stream()
                .map(CartItem::getMenuItem)
                .filter(Objects::nonNull)
                .map(MenuItem::getMenu)
                .filter(Objects::nonNull)
                .map(Menu::getRestaurant)
                .findFirst()
                .orElse(null);
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getId());
        dto.setStatus(order.getStatus().name());
        dto.setSubtotal(order.getSubtotal());
        dto.setTotalTax(order.getTotalTax());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        dto.setPickupTime(order.getPickupTime());
        dto.setCustomerName(order.getCustomerName());
        dto.setCustomerPhone(order.getCustomerPhone());
        dto.setCustomerNotes(order.getCustomerNotes());
        dto.setOrderType(order.getOrderType().name());
        dto.setRestaurantId(order.getRestaurant().getId());
        dto.setRestaurantName(order.getRestaurant().getName());

        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(this::convertItemToDTO)
                .toList();
        dto.setItems(itemDTOs);

        return dto;
    }

    private OrderItemDTO convertItemToDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());
        dto.setMenuItemId(item.getMenuItemId());
        dto.setMenuItemName(item.getMenuItemName());
        dto.setUnitPrice(item.getMenuItemPrice());
        dto.setQuantity(item.getQuantity());
        dto.setLineTotal(item.getTotalPrice());

        List<OrderItemOptionDTO> optionDTOs = item.getOptions().stream()
                .map(this::convertOptionToDTO)
                .toList();
        dto.setOptions(optionDTOs);

        return dto;
    }

    private OrderItemOptionDTO convertOptionToDTO(OrderItemOption option) {
        return OrderItemOptionDTO.builder()
                .optionId(option.getId())
                .optionGroupName(option.getGroupName())
                .optionName(option.getOptionName())
                .extraPrice(option.getExtraPrice())
                .build();
    }
}


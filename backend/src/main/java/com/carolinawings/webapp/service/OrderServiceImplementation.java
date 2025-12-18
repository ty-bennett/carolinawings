/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.OrderDTO;
import com.carolinawings.webapp.dto.OrderResponseDTO;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.model.Order;
import com.carolinawings.webapp.repository.MenuItemRepository;
import com.carolinawings.webapp.repository.OrderRepository;
import com.carolinawings.webapp.repository.RestaurantRepository;
import com.carolinawings.webapp.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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
}


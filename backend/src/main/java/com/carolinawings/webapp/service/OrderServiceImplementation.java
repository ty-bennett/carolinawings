/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.OrderDTO;
import com.carolinawings.webapp.dto.OrderResponse;
import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.model.MenuItem;
import com.carolinawings.webapp.model.Order;
import com.carolinawings.webapp.model.Restaurant;
import com.carolinawings.webapp.repository.MenuItemRepository;
import com.carolinawings.webapp.repository.OrderRepository;
import com.carolinawings.webapp.repository.RestaurantRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
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

    public OrderServiceImplementation(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderResponse getAllOrdersByRestaurantPaged(Integer pageNumber, Integer pageSize, Long restaurantId) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new ResourceNotFoundException("Restaurant", "id", restaurantId));
        Page<Order> orders = orderRepository.findOrdersByRestaurantAssignedTo(restaurantId, pageDetails);

        if (orders.isEmpty())
            throw new APIException("No orders present");
        List<OrderDTO> orderDTOS = orders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .toList();

        OrderResponse oR = new OrderResponse();
        oR.setContent(orderDTOS);
        oR.setPageNumber(orders.getNumber());
        oR.setPageSize(orders.getSize());
        oR.setTotalElements(orders.getTotalElements());
        oR.setTotalPages(orders.getTotalPages());
        oR.setLastPage(orders.isLast());
        return oR;
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

    public OrderDTO createOrderByRestaurant(Long id, @Valid OrderDTO orderDTO) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Restaurant", "id", id));
        Order order = modelMapper.map(orderDTO, Order.class);
        List<Integer> menuItemIdList = orderDTO.getListOfItems();
        List<MenuItem> menuItemList = menuItemIdList.stream().map(menuItemRepository::findById)
                        .filter(Optional::isPresent).map(Optional::get).toList();
        BigDecimal totalAmount = menuItemList.stream()
                .map(MenuItem::getPrice)
                .filter(Objects::nonNull) //remove null elements, if they are there
                .reduce(BigDecimal.ZERO, BigDecimal::add); // sum prices starting from zero
        order.setRestaurantAssignedTo(restaurant.getId()); //set restaurant id it's assigned to
        order.setOrderAmount(totalAmount); //set total amount to the calculated price
        order.setListOfMenuItems(menuItemList);
        orderRepository.save(order); //save order to repository
        return modelMapper.map(order, OrderDTO.class); //convert back to DTO to return to User
    }
}


package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.OrderDTO;

public interface OrderNotificationService {
    public void notifyNewOrder(Long restaurantId, OrderDTO order);
    public void notifyOrderStatusChange(Long restaurantId, OrderDTO order);

}

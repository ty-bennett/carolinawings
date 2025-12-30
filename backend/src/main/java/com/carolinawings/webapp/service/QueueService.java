package com.carolinawings.webapp.service;


public interface QueueService {
    void createQueueForRestaurant(Long restaurantId);
    void deleteQueueForRestaurant(Long restaurantId);
}

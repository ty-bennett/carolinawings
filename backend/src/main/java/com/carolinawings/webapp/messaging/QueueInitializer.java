package com.carolinawings.webapp.messaging;

import com.carolinawings.webapp.repository.RestaurantRepository;
import com.carolinawings.webapp.service.QueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QueueInitializer implements CommandLineRunner {

    private final RestaurantRepository restaurantRepository;
    private final QueueService queueService;

    public QueueInitializer(RestaurantRepository restaurantRepository, QueueService queueService) {
        this.restaurantRepository = restaurantRepository;
        this.queueService = queueService;
    }

    @Override
    public void run(String... args) {
        log.info("Initializing RabbitMQ queues for existing restaurants...");

        restaurantRepository.findAll().forEach(restaurant -> {
            queueService.createQueueForRestaurant(restaurant.getId());
        });

        log.info("Queue initialization complete");
    }
}
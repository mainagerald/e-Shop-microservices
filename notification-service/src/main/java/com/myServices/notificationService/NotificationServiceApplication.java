package com.myServices.notificationService;

import lombok.extern.slf4j.Slf4j;
import com.myServices.notificationService.event.OrderPlacedEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "notificationTopic")
    public void HandleNotification(OrderPlacedEvent orderPlacedEvent)
    {
//email notification
        log.info("Received notification for order: {}", orderPlacedEvent.getOrderNumber());
    }

}

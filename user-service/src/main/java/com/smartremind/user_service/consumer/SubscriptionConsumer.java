package com.smartremind.user_service.consumer;


import com.smartremind.user_service.event.SubscriptionActivationEvent;

import com.smartremind.user_service.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class SubscriptionConsumer {
    private static final Logger log = LoggerFactory.getLogger(SubscriptionConsumer.class);

    private final UserService userService;

    public SubscriptionConsumer(UserService userService){
        this.userService = userService;
    }


    @KafkaListener(
            topics = "subscription-events",
            //group id should be removed
            groupId = "user-service-group"

    )
    public  void consume(SubscriptionActivationEvent event){


    userService.activateUserSubscription(event);


    }


}

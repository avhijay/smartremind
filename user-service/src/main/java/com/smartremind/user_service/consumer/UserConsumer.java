package com.smartremind.user_service.consumer;


import com.smartremind.user_service.event.SubscriptionActivationEvent;

import com.smartremind.user_service.event.UserCreationEvent;
import com.smartremind.user_service.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserConsumer {
    private static final Logger log = LoggerFactory.getLogger(UserConsumer.class);

    private final UserService userService;

    public UserConsumer(UserService userService){
        this.userService = userService;
    }


    @KafkaListener(
            topics = "subscription-events",
            //group id should be removed
            groupId = "user-service-group"

    )
    public  void activateUserSubscription(SubscriptionActivationEvent event){


    userService.activateUserSubscription(event);


    }



    @KafkaListener(
            topics = "users-events",
            groupId = "user-service-group"

    )
    //UserCreationEvent userCreationEvent usage of dto specified event
    public  void createUser(UserCreationEvent userCreationEvent){
        log.debug("User Creation Event : {}  received " ,userCreationEvent.username());
        userService.createUser(userCreationEvent);

    }


}

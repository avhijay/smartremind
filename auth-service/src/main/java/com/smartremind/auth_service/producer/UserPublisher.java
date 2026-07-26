package com.smartremind.auth_service.producer;

import com.smartremind.auth_service.events.UserCreationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserPublisher {


    private  static  final String USER_TOPIC = "users-events";

    private  static Logger log  = LoggerFactory.getLogger(UserPublisher.class);

    private final KafkaTemplate<String , UserCreationEvent> kafkaTemplate;

    public UserPublisher(KafkaTemplate<String , UserCreationEvent> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;

    }

    public  void  publishUser (UserCreationEvent event){
        kafkaTemplate.send(USER_TOPIC ,event.username() ,event);

    }




}

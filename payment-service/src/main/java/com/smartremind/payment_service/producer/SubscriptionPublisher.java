package com.smartremind.payment_service.producer;


import com.smartremind.payment_service.events.SubscriptionActivationEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionPublisher {

    // assign kafka topic for the class

    private  static  final String SUBSCRIPTION_TOPIC = "subscription-events";

    //kafka template injection
    private  final KafkaTemplate<String , SubscriptionActivationEvent> kafkaTemplate;

    public SubscriptionPublisher(KafkaTemplate<String,SubscriptionActivationEvent> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }


public void publishSubscriptionEvent( SubscriptionActivationEvent event ){

        kafkaTemplate.send(SUBSCRIPTION_TOPIC,event.username(),event);
}




}

package com.smartremind.payment_service.scheduler;


import com.smartremind.payment_service.entity.KafkaOutboxData;
import com.smartremind.payment_service.events.SubscriptionActivationEvent;
import com.smartremind.payment_service.producer.SubscriptionPublisher;
import com.smartremind.payment_service.repository.OutBoxDataRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
public class KafkaEventOutboxService {

    private final OutBoxDataRepository outboxDataRepository;

    private final SubscriptionPublisher publisher;

    public KafkaEventOutboxService(OutBoxDataRepository outboxDataRepository , SubscriptionPublisher publisher){
        this.outboxDataRepository=outboxDataRepository;
        this.publisher = publisher;
    }



    private static final Logger log = LoggerFactory.getLogger(KafkaEventOutboxService.class);

    @Scheduled(fixedDelay = 10000)
    public void PublishEvent(){

        log.info("Scheduler active | Publishing to Kafka ");



            List<KafkaOutboxData> data = outboxDataRepository.findByPublishedFalse();


            for (KafkaOutboxData Outboxdata :data ){
                SubscriptionActivationEvent event = outboxToEvent(Outboxdata);

                try {
                    publisher.publishSubscriptionEvent(event).get();
                    Outboxdata.setPublished(true);
                    outboxDataRepository.save(Outboxdata);

                    log.info("Outbox event published to kafka id {}" , Outboxdata.getId());



                }catch (Exception e){
                    log.info("Failure to publish in Kafka  id {}" , Outboxdata.getId());



                }


            }




        }







    private SubscriptionActivationEvent outboxToEvent(KafkaOutboxData data){
        SubscriptionActivationEvent event = new SubscriptionActivationEvent(data.getUserName(),
                data.getSubscriptionStatus(),data.getExpiresAt(),
                data.getUniqueId(), data.getSubscriptionId());

        return event;


    }



}

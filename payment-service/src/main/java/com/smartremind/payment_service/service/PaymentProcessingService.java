package com.smartremind.payment_service.service;


import com.smartremind.payment_service.dto.provider.PaymentProviderRequestDto;

import com.smartremind.payment_service.dto.provider.PaymentProviderResponseDTO;
import com.smartremind.payment_service.dto.purchase.SubscriptionPurchaseResponseDTO;
import com.smartremind.payment_service.entity.KafkaOutboxData;
import com.smartremind.payment_service.entity.PaymentOutboxData;
import com.smartremind.payment_service.entity.SubscriptionPayment;
import com.smartremind.payment_service.entity.SubscriptionPlans;
import com.smartremind.payment_service.enums.PaymentOutboxStatus;
import com.smartremind.payment_service.enums.PaymentStatus;

import com.smartremind.payment_service.enums.SubscriptionStatus;
import com.smartremind.payment_service.exception.PaymentDoesNotExistException;
import com.smartremind.payment_service.exception.SubscriptionPlanNotFoundException;
import com.smartremind.payment_service.producer.SubscriptionPublisher;
import com.smartremind.payment_service.repository.OutBoxDataRepository;
import com.smartremind.payment_service.repository.PaymentOutboxRepository;
import com.smartremind.payment_service.repository.SubscriptionPaymentRepository;
import com.smartremind.payment_service.repository.SubscriptionPlanRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@Service
public class PaymentProcessingService {

    private static  final String PAYMENT_ID = "Payment-";


    // assign kafka topic for the class



// retries allowed for payment
    private  final  int maxRetryAllowed ;

    private static  final Logger log = LoggerFactory.getLogger(PaymentProcessingService.class);

    private final SubscriptionPaymentRepository subscriptionPaymentRepository;
    private final SubscriptionPlanRepository  subscriptionPlanRepository;

    private final OutBoxDataRepository outBoxDataRepository;
    private  final PaymentOutboxRepository paymentOutboxRepository;

    // assign region currency


    //kafka template injection
    private  final SubscriptionPublisher publisher;


    public PaymentProcessingService(SubscriptionPaymentRepository subscriptionPaymentRepository , SubscriptionPlanRepository subscriptionPlanRepository ,
                                    @Value("${payment.retry.max-attempt}")  int maxRetryAllowed  ,
                                    SubscriptionPublisher publisher , OutBoxDataRepository outBoxDataRepository, PaymentOutboxRepository paymentOutboxRepository

    ){

        this.subscriptionPaymentRepository = subscriptionPaymentRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.maxRetryAllowed = maxRetryAllowed;


        this.publisher = publisher;
        this.outBoxDataRepository=outBoxDataRepository;
        this.paymentOutboxRepository = paymentOutboxRepository;


    }




@Transactional
public void  processPayment ( PaymentProviderResponseDTO  paymentResponse , PaymentOutboxData paymentOutboxData ) {

      log.debug("Getting Subscription Plan : {}" , paymentOutboxData.getSubscriptionId());
    SubscriptionPlans plan = subscriptionPlanRepository.findById(paymentOutboxData.getSubscriptionId()).orElseThrow(()->
            new SubscriptionPlanNotFoundException("Subscription plan not found with id : "+paymentOutboxData.getSubscriptionId()));



    // get Payment object
    SubscriptionPayment payment = subscriptionPaymentRepository.findByIdempotencyKey(paymentOutboxData.getIdempotencyKey()).orElseThrow(()->
            new PaymentDoesNotExistException("No Payment Exist for key : {}"+paymentOutboxData.getIdempotencyKey()));




    //if success
    if (paymentResponse.status()==PaymentStatus.SUCCESS  && Objects.equals(payment.getPaymentId(), paymentResponse.paymentId())){
log.info("Payment Provider  : Payment Success");

        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setProviderTransactionId(paymentResponse.providerTransactionId());

        payment.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setAmountPaidAt(Instant.now());
        payment.setActivatedAt(Instant.now());
        payment.setExpiresAt(Instant.now().plus(plan.getPlanDurationDays(), ChronoUnit.DAYS));
        log.info(" Successful Payment :  save to database | Pending " );

        subscriptionPaymentRepository.save(payment);

        log.info("Payment : {}   save to database | Success" , payment.getPaymentId());

        // Publish Event  on Outbox


        // save to outbox
        log.info("Publishing Payment  to Outbox  ");

        KafkaOutboxData data = paymentToOutbox(payment);
        outBoxDataRepository.save(data);


        // update process payment Outbox
         paymentOutboxData.setPaymentOutboxStatus(PaymentOutboxStatus.SUCCESS);
         paymentOutboxRepository.save(paymentOutboxData);






    }else{

// to be implemented together with partial failure code -
       // Provider Webhooks
        //kafka
       // Payment Reconciliation


        payment.setPaymentStatus(PaymentStatus.FAILED);
        payment.setProviderTransactionId(paymentResponse.providerTransactionId());
        paymentOutboxData.setPaymentOutboxStatus(PaymentOutboxStatus.FAILED);
        paymentOutboxRepository.save(paymentOutboxData);

        payment.setSubscriptionStatus(SubscriptionStatus.NOT_PURCHASED);
        payment.setPaymentStatus(PaymentStatus.FAILED);


        subscriptionPaymentRepository.save(payment);







    }






}


//@Transactional
//private SubscriptionPurchaseResponseDTO retryPayment(){
//
//}













    private SubscriptionPurchaseResponseDTO subscriptionPaymentToResponseHelper(SubscriptionPayment subscriptionPayment){
        return new SubscriptionPurchaseResponseDTO(subscriptionPayment.getPaymentId(),
                subscriptionPayment.getSubscriptionPlanId(),subscriptionPayment.getAutoRenew()
                ,subscriptionPayment.getSubscriptionStatus(),subscriptionPayment.getPaymentStatus(), subscriptionPayment.getExpiresAt());

    }


private PaymentProviderRequestDto paymentToProviderRequestHelper(SubscriptionPayment payment ){

        //Get the payment amount
    SubscriptionPlans plan  = subscriptionPlanRepository.findById(payment.getSubscriptionPlanId())
            .orElseThrow(()->new SubscriptionPlanNotFoundException("No subscription exist by the id "+payment.getSubscriptionPlanId()));


    return new PaymentProviderRequestDto(payment.getPaymentId(),plan.getAmount(),payment.getCurrency());



}



    private KafkaOutboxData paymentToOutbox(SubscriptionPayment payment){

        String id = "Unique"+UUID.randomUUID();

        KafkaOutboxData data = KafkaOutboxData.builder()
                .userName(payment.getUsername())
                .expiresAt(payment.getExpiresAt())
                .subscriptionStatus(payment.getSubscriptionStatus())
                .uniqueId(id)
                .subscriptionId(payment.getSubscriptionPlanId())
                .published(false)
                .build();
        return data;


    }






}

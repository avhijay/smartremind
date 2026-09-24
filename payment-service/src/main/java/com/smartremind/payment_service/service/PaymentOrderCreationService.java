package com.smartremind.payment_service.service;

import com.smartremind.payment_service.dto.provider.PaymentProviderResponseDTO;
import com.smartremind.payment_service.entity.PaymentOutboxData;
import com.smartremind.payment_service.entity.SubscriptionPayment;
import com.smartremind.payment_service.enums.PaymentOutboxStatus;
import com.smartremind.payment_service.enums.PaymentStatus;
import com.smartremind.payment_service.exception.PaymentDoesNotExistException;

import com.smartremind.payment_service.repository.PaymentOutboxRepository;
import com.smartremind.payment_service.repository.SubscriptionPaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
public class PaymentOrderCreationService {


    private  final PaymentOutboxRepository paymentOutboxRepository;
   private  final SubscriptionPaymentRepository subscriptionPaymentRepository;
   private  static final Logger log = LoggerFactory.getLogger(PaymentOrderCreationService.class);

    public PaymentOrderCreationService (PaymentOutboxRepository paymentOutboxRepository  , SubscriptionPaymentRepository subscriptionPaymentRepository){

        this.paymentOutboxRepository = paymentOutboxRepository;
        this.subscriptionPaymentRepository = subscriptionPaymentRepository;

    }





    // update payment entity with provider response ( create order )
    @Transactional
    public void orderCreation(PaymentProviderResponseDTO responseDTO){
        log.info("Request Create / Update Order for  Subscription payment Entity  : Received ");

        // updating subscription payment db
        SubscriptionPayment subscriptionPayment = subscriptionPaymentRepository
                .findByPaymentId(responseDTO.paymentId())
                .orElseThrow(()-> new PaymentDoesNotExistException("No payment found by referring to provider response  | id :  "+responseDTO.paymentId()));

        subscriptionPayment.setProviderOrderId(responseDTO.providerOrderId());


            subscriptionPayment.setPaymentStatus(PaymentStatus.PROCESSING);



        subscriptionPaymentRepository.save(subscriptionPayment);


        //updating OutBox
        PaymentOutboxData paymentOutboxData = paymentOutboxRepository
                .findByPaymentId(responseDTO.paymentId())
                .orElseThrow(()-> new PaymentDoesNotExistException("No payment found by referring to provider response  | id :  "+responseDTO.paymentId()));
        paymentOutboxData.setPaymentOutboxStatus(PaymentOutboxStatus.PROCESSING);
        paymentOutboxRepository.save(paymentOutboxData);
        log.info("Updating  PaymentOutbox Status : Success ");


    }





}

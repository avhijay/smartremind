package com.smartremind.payment_service.service;


import com.smartremind.payment_service.dto.purchase.PaymentCreationResponseDTO;
import com.smartremind.payment_service.dto.purchase.SubscriptionPurchaseRequestDTO;
import com.smartremind.payment_service.dto.purchase.SubscriptionPurchaseResponseDTO;
import com.smartremind.payment_service.entity.PaymentOutboxData;
import com.smartremind.payment_service.entity.SubscriptionPayment;
import com.smartremind.payment_service.entity.SubscriptionPlans;
import com.smartremind.payment_service.enums.Currency;
import com.smartremind.payment_service.enums.PaymentOutboxStatus;
import com.smartremind.payment_service.enums.PaymentStatus;
import com.smartremind.payment_service.enums.SubscriptionStatus;
import com.smartremind.payment_service.exception.DuplicatePaymentException;
import com.smartremind.payment_service.exception.PaymentDoesNotExistException;
import com.smartremind.payment_service.exception.SubscriptionAlreadyExistException;
import com.smartremind.payment_service.exception.SubscriptionPlanNotFoundException;
import com.smartremind.payment_service.repository.PaymentOutboxRepository;
import com.smartremind.payment_service.repository.SubscriptionPaymentRepository;
import com.smartremind.payment_service.repository.SubscriptionPlanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PaymentCreationService {



    private static  final String PAYMENT_ID = "Payment-";
    private final Currency currency;


    private final PaymentOutboxRepository paymentOutboxRepository;
    private  final SubscriptionPaymentRepository subscriptionPaymentRepository;
    private  final SubscriptionPlanRepository subscriptionPlanRepository ;
    private static final Logger log = LoggerFactory.getLogger(PaymentCreationService.class);

    public PaymentCreationService(PaymentOutboxRepository paymentOutboxRepository, SubscriptionPaymentRepository subscriptionPaymentRepository ,
                                  SubscriptionPlanRepository subscriptionPlanRepository ,  @Value("${payment.region.local-currency}") Currency currency){

        this.paymentOutboxRepository = paymentOutboxRepository;
        this.subscriptionPaymentRepository = subscriptionPaymentRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.currency = currency;

    }



@Transactional
    public PaymentCreationResponseDTO createPayment(SubscriptionPurchaseRequestDTO request , String idempotencyKey){

        //check if plan already exist by the username

        log.info("Request create payment : Received  | Process payment : Pending  ");


        if (subscriptionPaymentRepository.existsByUsername(request.username()) ){

            log.debug("Getting payment if Exist by username :{}" , request.username());

            SubscriptionPayment payment = subscriptionPaymentRepository.findByUsername(request.username())
                    .orElseThrow(()-> new PaymentDoesNotExistException("Payment does not exist for the username "+request.username()));




            // throw exception if payment exist for the username with a success payment
            if (payment.getSubscriptionStatus() == SubscriptionStatus.ACTIVE ) {
                log.debug("Checking if Payment Subscription is active");

                throw new SubscriptionAlreadyExistException("User cannot buy 2 quantity of the same subscription plan  ");
            }

            if (payment.getSubscriptionStatus() == SubscriptionStatus.NOT_ACTIVATED){

                log.info("Payment confirmed but Subscription Status : {}", SubscriptionStatus.NOT_ACTIVATED);

                throw new SubscriptionAlreadyExistException("Subscription plan exist and current status of plan :"+SubscriptionStatus.NOT_ACTIVATED);
            }



        }
// idempotency key check

        log.info("Payment check for if exist by Idempotency key : {}" , idempotencyKey );

        if (subscriptionPaymentRepository.existByIdempotencyKey(idempotencyKey)){
            SubscriptionPayment payment =  subscriptionPaymentRepository.findByIdempotencyKey(idempotencyKey)
                    .orElseThrow(()->new PaymentDoesNotExistException("PAYMENT DOES NOT EXIST"));

            log.info("Duplicate payment found  | idempotency key : {} " , idempotencyKey);

            throw  new DuplicatePaymentException("PAYMENT ALREADY EXIST CURRENT STATUS "+ payment.getPaymentStatus());



        }
        //create payment but don't process

        // get the sub plan selected by the client

        log.debug("Getting Subscription plan details  for the plan : {}", request.subscriptionPlan());

        SubscriptionPlans plan = subscriptionPlanRepository.findById(request.subscriptionPlan())
                .orElseThrow(()->new SubscriptionPlanNotFoundException("No subscription exist with the provided  id "));

        // generate payment id
        log.info("Generating PaymentId ");

        String paymentId  =  PAYMENT_ID + UUID.randomUUID();


        SubscriptionPayment payment = paymentCreationMapper(request,idempotencyKey,paymentId);


        subscriptionPaymentRepository.save(payment);

        log.info("Payment creation : Success | Payment processing : Pending ");


        PaymentOutboxData outboxData = paymentToOutboxMapper(payment);
        paymentOutboxRepository.save(outboxData);


        PaymentCreationResponseDTO paymentCreationResponseDTO = new PaymentCreationResponseDTO(paymentId,payment.getPaymentStatus());
        return paymentCreationResponseDTO;





    }





    private  SubscriptionPayment paymentCreationMapper(SubscriptionPurchaseRequestDTO request , String idempotencyKey , String paymentId){


        SubscriptionPlans plan = subscriptionPlanRepository.findById(request.subscriptionPlan())
                .orElseThrow(()->new SubscriptionPlanNotFoundException("No subscription exist with the provided  id "));


        SubscriptionPayment payment = SubscriptionPayment.builder()
                .paymentId(paymentId)
                .username(request.username())
                .subscriptionPlanId(request.subscriptionPlan())
                .subscriptionStatus(SubscriptionStatus.NOT_ACTIVATED)
                .autoRenew(request.autoRenew())
                .amount(plan.getAmount())
                .currency(currency)
                .paymentStatus(PaymentStatus.PENDING)
                .paymentMethod(request.paymentMethod())
                .idempotencyKey(idempotencyKey)
                .build();


        return payment;

    }


    private PaymentOutboxData paymentToOutboxMapper(SubscriptionPayment subscriptionPayment){

        PaymentOutboxData data = PaymentOutboxData.builder()
                .paymentId(subscriptionPayment.getPaymentId())
                .paymentOutboxStatus(PaymentOutboxStatus.PENDING)
                .currentRetryCount(subscriptionPayment.getCurrentRetryCount())
                .paymentMethod(subscriptionPayment.getPaymentMethod())
                .amount(subscriptionPayment.getAmount())
                .idempotencyKey(subscriptionPayment.getIdempotencyKey())
                .currency(subscriptionPayment.getCurrency())
                                                                                .

                build();


return  data;

    }



}

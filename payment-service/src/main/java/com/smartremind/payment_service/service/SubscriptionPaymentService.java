package com.smartremind.payment_service.service;


import com.smartremind.payment_service.dto.provider.PaymentProviderRequestDTO;

import com.smartremind.payment_service.dto.provider.PaymentProviderResponseDTO;
import com.smartremind.payment_service.dto.purchase.SubscriptionPurchaseRequestDTO;
import com.smartremind.payment_service.dto.purchase.SubscriptionPurchaseResponseDTO;
import com.smartremind.payment_service.entity.SubscriptionPayment;
import com.smartremind.payment_service.entity.SubscriptionPlans;
import com.smartremind.payment_service.enums.Currency;
import com.smartremind.payment_service.enums.PaymentStatus;

import com.smartremind.payment_service.enums.SubscriptionStatus;
import com.smartremind.payment_service.events.SubscriptionActivationEvent;
import com.smartremind.payment_service.exception.DuplicatePaymentException;
import com.smartremind.payment_service.exception.PaymentDoesNotExistException;
import com.smartremind.payment_service.exception.SubscriptionAlreadyExistException;
import com.smartremind.payment_service.exception.SubscriptionPlanNotFoundException;
import com.smartremind.payment_service.payment_provider.PaymentProvider;
import com.smartremind.payment_service.producer.SubscriptionPublisher;
import com.smartremind.payment_service.repository.SubscriptionPaymentRepository;
import com.smartremind.payment_service.repository.SubscriptionPlanRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@Service
public class SubscriptionPaymentService {

    private static  final String PAYMENT_ID = "Payment-";


    // assign kafka topic for the class



// retries allowed for payment
    private  final  int maxRetryAllowed ;

    private static  final Logger log = LoggerFactory.getLogger(SubscriptionPaymentService.class);

    private final SubscriptionPaymentRepository subscriptionPaymentRepository;
    private final SubscriptionPlanRepository  subscriptionPlanRepository;
    private final PaymentProvider paymentProvider;

    // assign region currency
    private final Currency currency;

    //kafka template injection
    private  final SubscriptionPublisher publisher;


    public  SubscriptionPaymentService(SubscriptionPaymentRepository subscriptionPaymentRepository , SubscriptionPlanRepository subscriptionPlanRepository ,
                                       @Value("${payment.retry.max-attempt}")  int maxRetryAllowed , PaymentProvider paymentProvider ,
                                       @Value("${payment.region.local-currency}") Currency currency ,
                                       SubscriptionPublisher publisher

    ){

        this.subscriptionPaymentRepository = subscriptionPaymentRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.maxRetryAllowed = maxRetryAllowed;
        this.paymentProvider = paymentProvider;
        this.currency=currency;
        this.publisher = publisher;


    }



    // payment creation
    //save payment with PENDING STATUS
    public SubscriptionPurchaseResponseDTO createPayment(SubscriptionPurchaseRequestDTO request , String idempotencyKey){

        //check if plan already exist by the username

        if (subscriptionPaymentRepository.existsByUsername(request.username()) ){
            SubscriptionPayment payment = subscriptionPaymentRepository.findByUsername(request.username())
                    .orElseThrow(()-> new PaymentDoesNotExistException("Payment does not exist for the username "+request.username()));


            // throw exception if payment exist for the username with a success payment
            if (payment.getSubscriptionStatus() == SubscriptionStatus.ACTIVE ) {

                throw new SubscriptionAlreadyExistException("User cannot buy 2 quantity of the same subscription plan  ");
            }

            if (payment.getSubscriptionStatus() == SubscriptionStatus.NOT_ACTIVATED){
                throw new SubscriptionAlreadyExistException("Subscription plan exist and current status of plan :"+SubscriptionStatus.NOT_ACTIVATED);
            }

        }
// idempotency key check
       else if (subscriptionPaymentRepository.existByIdempotencyKey(idempotencyKey)){
           SubscriptionPayment payment =  subscriptionPaymentRepository.findByIdempotencyKey(idempotencyKey)
                   .orElseThrow(()->new PaymentDoesNotExistException("PAYMENT DOES NOT EXIST"));


           throw  new DuplicatePaymentException("PAYMENT ALREADY EXIST CURRENT STATUS "+ payment.getPaymentStatus());



        }
       //create payment but don't process

           // get the sub plan selected by the client
        SubscriptionPlans plan = subscriptionPlanRepository.findById(request.subscriptionPlan())
                .orElseThrow(()->new SubscriptionPlanNotFoundException("No subscription exist with the provided  id "));

        // generate payment id
        String paymentId  =  PAYMENT_ID +UUID.randomUUID();

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
                    subscriptionPaymentRepository.save(payment);

     return processPayment(payment);




    }




@Transactional
public SubscriptionPurchaseResponseDTO processPayment (SubscriptionPayment payment) {
        PaymentProviderRequestDTO paymentRequest  = paymentToProviderRequestHelper(payment);

        // sending request dto to payment provider
      PaymentProviderResponseDTO   paymentResponse =  paymentProvider.processCompletedPayment(paymentRequest);

    SubscriptionPlans plan = subscriptionPlanRepository.
            findById(payment.getSubscriptionPlanId()).
            orElseThrow(()->new SubscriptionPlanNotFoundException("Subscription plan not found with id : "+payment.getSubscriptionPlanId()));


// change the fields for the payment

    //if success
    if (paymentResponse.status()==PaymentStatus.SUCCESS  && Objects.equals(payment.getPaymentId(), paymentResponse.paymentId())){


        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setProviderTransactionId(paymentResponse.providerTransactionId());

        payment.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setAmountPaidAt(Instant.now());
        payment.setActivatedAt(Instant.now());
        payment.setExpiresAt(Instant.now().plus(plan.getPlanDurationDays(), ChronoUnit.DAYS));
        subscriptionPaymentRepository.save(payment);

        // create Kafka event

        SubscriptionActivationEvent event = new SubscriptionActivationEvent(payment.getUsername()
                ,payment.getSubscriptionStatus(),payment.getExpiresAt());


        // publish in kafka
       publisher.publishSubscriptionEvent(event);




    }else{

// to be implemented together with partial failure code -
       // Provider Webhooks
        //kafka
       // Payment Reconciliation


        payment.setPaymentStatus(PaymentStatus.FAILED);
        payment.setProviderTransactionId(paymentResponse.providerTransactionId());

        payment.setSubscriptionStatus(SubscriptionStatus.NOT_PURCHASED);
        payment.setPaymentStatus(PaymentStatus.FAILED);


        subscriptionPaymentRepository.save(payment);







    }



    SubscriptionPurchaseResponseDTO responseDTO = subscriptionPaymentToResponseHelper(payment);
    return responseDTO;


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


private PaymentProviderRequestDTO paymentToProviderRequestHelper(SubscriptionPayment payment ){

        //Get the payment amount
    SubscriptionPlans plan  = subscriptionPlanRepository.findById(payment.getSubscriptionPlanId())
            .orElseThrow(()->new SubscriptionPlanNotFoundException("No subscription exist by the id "+payment.getSubscriptionPlanId()));


    return new PaymentProviderRequestDTO(payment.getPaymentId(),plan.getAmount(),payment.getPaymentMethod(),payment.getCurrency());



}







}

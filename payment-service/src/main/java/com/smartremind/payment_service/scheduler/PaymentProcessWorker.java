package com.smartremind.payment_service.scheduler;


import ch.qos.logback.core.util.FixedDelay;
import com.smartremind.payment_service.dto.provider.PaymentProviderRequestDTO;
import com.smartremind.payment_service.dto.provider.PaymentProviderResponseDTO;
import com.smartremind.payment_service.entity.PaymentOutboxData;
import com.smartremind.payment_service.enums.PaymentOutboxStatus;
import com.smartremind.payment_service.payment_provider.PaymentProvider;
import com.smartremind.payment_service.repository.PaymentOutboxRepository;
import com.smartremind.payment_service.service.PaymentProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@EnableScheduling
@Component
public class PaymentProcessWorker {

    private static  final Logger log = LoggerFactory.getLogger(PaymentProcessWorker.class);

    private final PaymentProcessingService paymentProcessingService;
    private final PaymentProvider paymentProvider;
    private final PaymentOutboxRepository paymentOutboxRepository;

    public PaymentProcessWorker(PaymentProcessingService paymentProcessingService , PaymentProvider paymentProvider , PaymentOutboxRepository paymentOutboxRepository ){
        this.paymentProcessingService= paymentProcessingService;
        this.paymentProvider = paymentProvider;
        this.paymentOutboxRepository = paymentOutboxRepository;
    }


    @Scheduled(fixedDelay = 1000)
    public void processPayment(){

        List<PaymentOutboxData> payments = paymentOutboxRepository.findByPaymentOutboxStatus(PaymentOutboxStatus.PENDING);

        for (PaymentOutboxData  payment : payments){

            try {

                PaymentProviderRequestDTO providerRequest = paymentToProviderRequest(payment);
                PaymentProviderResponseDTO providerResponse = paymentProvider.processCompletedPayment(providerRequest);
                paymentProcessingService.processPayment(providerResponse,payment);



            }catch (Exception e ){

log.info("Unable to Process Payment : {}", e.getMessage());

            }


        }




    }


    private PaymentProviderRequestDTO paymentToProviderRequest(PaymentOutboxData data){

        PaymentProviderRequestDTO requestDTO = new PaymentProviderRequestDTO(data.getPaymentId(), data.getAmount(),data.getPaymentMethod(),data.getCurrency());

        return requestDTO;


    }







}

package com.smartremind.payment_service.scheduler;


import com.smartremind.payment_service.dto.provider.PaymentProviderRequestDto;
import com.smartremind.payment_service.dto.provider.PaymentProviderResponseDTO;
import com.smartremind.payment_service.entity.PaymentOutboxData;
import com.smartremind.payment_service.enums.PaymentOutboxStatus;
import com.smartremind.payment_service.payment_provider.PaymentProvider;
import com.smartremind.payment_service.repository.PaymentOutboxRepository;
import com.smartremind.payment_service.service.PaymentCreationService;
import com.smartremind.payment_service.service.PaymentOrderCreationService;
import com.smartremind.payment_service.service.PaymentProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@EnableScheduling
@Component
public class ProviderOrderCreationWorker {

    private static  final Logger log = LoggerFactory.getLogger(ProviderOrderCreationWorker.class);


    private final PaymentProvider paymentProvider;
    private final PaymentOutboxRepository paymentOutboxRepository;
    private final PaymentOrderCreationService paymentOrderCreationService ;

    public ProviderOrderCreationWorker( PaymentProvider paymentProvider ,
                                       PaymentOutboxRepository paymentOutboxRepository  , PaymentOrderCreationService paymentOrderCreationService){

        this.paymentProvider = paymentProvider;
        this.paymentOutboxRepository = paymentOutboxRepository;
        this.paymentOrderCreationService = paymentOrderCreationService;
    }


    @Scheduled(fixedDelay = 1000)
    public void processPayment() {

        log.info(" Process Payment Worker : Fetching payments with Status : Pending ");

        List<PaymentOutboxData> payments = paymentOutboxRepository.findByPaymentOutboxStatus(PaymentOutboxStatus.PENDING);

        for (PaymentOutboxData  payment : payments){

            try {

                PaymentProviderRequestDto providerRequest = paymentToProviderRequest(payment);


                log.info("Sending payment info to Payment Provider Service : {} " , providerRequest.paymentId());

         PaymentProviderResponseDTO providerResponse =  paymentProvider.createOrder(providerRequest);

         log.info("Payment Provider Service Response : Received ");
         // send the response  to orderCreation /  payment update

                log.info("Order creation Request sent to OrderCreation Service : {}" , providerResponse.paymentId());
paymentOrderCreationService.orderCreation(providerResponse);

            }catch (Exception e ){

log.info("Unable to Process Payment : {}", e.getMessage());

            }


        }




    }


    private PaymentProviderRequestDto paymentToProviderRequest(PaymentOutboxData data){

        PaymentProviderRequestDto requestDTO = new PaymentProviderRequestDto(data.getPaymentId(), data.getAmount(),data.getCurrency());

        return requestDTO;


    }







}

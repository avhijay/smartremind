package com.smartremind.payment_service.payment_provider;

import com.smartremind.payment_service.dto.provider.PaymentProviderRequestDto;
import com.smartremind.payment_service.dto.provider.PaymentProviderResponseDTO;
import com.smartremind.payment_service.dto.razorpay.RazorPayRequestDto;
import com.smartremind.payment_service.dto.razorpay.RazorPayResponseDto;
import com.smartremind.payment_service.entity.PaymentOutboxData;
import com.smartremind.payment_service.entity.SubscriptionPayment;
import com.smartremind.payment_service.enums.PaymentOutboxStatus;
import com.smartremind.payment_service.enums.PaymentStatus;
import com.smartremind.payment_service.exception.PaymentDoesNotExistException;
import com.smartremind.payment_service.exception.PaymentProviderException;
import com.smartremind.payment_service.repository.PaymentOutboxRepository;
import com.smartremind.payment_service.repository.SubscriptionPaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public  class RazorPayPaymentProvider implements PaymentProvider{

    private final RestClient razorPayrestClient;
    private final SubscriptionPaymentRepository subscriptionPaymentRepository ;
    private final PaymentOutboxRepository paymentOutboxRepository;
    private  final Logger log = LoggerFactory.getLogger(RazorPayPaymentProvider.class);


    public RazorPayPaymentProvider (RestClient restClient , SubscriptionPaymentRepository subscriptionPaymentRepository , PaymentOutboxRepository paymentOutboxRepository){
        this.razorPayrestClient = restClient;
        this.subscriptionPaymentRepository = subscriptionPaymentRepository;
        this.paymentOutboxRepository = paymentOutboxRepository;

    }


    @Override
    @Transactional
    public PaymentProviderResponseDTO createOrder(PaymentProviderRequestDto paymentProviderRequestDto) {

        log.info("Request : Provider order creation request | received ");


        RazorPayRequestDto razorPayRequest = mapToRazorPayRequest(paymentProviderRequestDto);


        try {

            log.info("Request : Provider order creation request | Sending to Razor pay : {} " , razorPayRequest.receipt());
            RazorPayResponseDto razorPayResponse = razorPayrestClient.post()
                    .uri("/v1/orders")
                    .body(razorPayRequest)
                    .retrieve()
                    .body(RazorPayResponseDto.class);

            assert razorPayResponse != null;
            log.info("Request : Provider order creation request | Success");
            PaymentProviderResponseDTO responseDTO = mapToPaymentProviderResponse(razorPayResponse);
            return responseDTO;

        } catch (Exception e) {
            log.info("Request : Provider order creation request | Failed  : {}", e.getMessage());
            throw  new PaymentProviderException("Exception occurred while creating order in Razor pay"+e.getMessage());
        }









    }


















    // receipt is equivalent yo payment Id

    private RazorPayRequestDto mapToRazorPayRequest(PaymentProviderRequestDto providerRequest){

        long amount = providerRequest.amount().movePointRight(2).longValueExact();

        RazorPayRequestDto razorPayRequest = new RazorPayRequestDto(amount,providerRequest.currency(), providerRequest.paymentId());
        return razorPayRequest;


    }

    private PaymentProviderResponseDTO mapToPaymentProviderResponse(RazorPayResponseDto razorPayResponseDto){

        BigDecimal amountDue = BigDecimal.valueOf(razorPayResponseDto.amount()).movePointLeft(2);


        PaymentProviderResponseDTO providerResponse =  new
                PaymentProviderResponseDTO
                (razorPayResponseDto.receipt(),amountDue, razorPayResponseDto.id(), razorPayResponseDto.status());

        return providerResponse;



    }




}

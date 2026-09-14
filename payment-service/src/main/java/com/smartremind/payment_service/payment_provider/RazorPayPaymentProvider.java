package com.smartremind.payment_service.payment_provider;

import com.smartremind.payment_service.dto.provider.PaymentProviderRequestDto;
import com.smartremind.payment_service.dto.provider.PaymentProviderResponseDTO;
import com.smartremind.payment_service.dto.razorpay.RazorPayRequestDto;
import com.smartremind.payment_service.dto.razorpay.RazorPayResponseDto;
import com.smartremind.payment_service.enums.PaymentStatus;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.UUID;

public  class RazorPayPaymentProvider implements PaymentProvider{

    private final RestClient razorPayrestClient;

    public RazorPayPaymentProvider (RestClient restClient){
        this.razorPayrestClient = restClient;

    }


    @Override
    public PaymentProviderResponseDTO createOrder(PaymentProviderRequestDto paymentProviderRequestDto) {


        RazorPayRequestDto razorPayRequest = mapToRazorPayRequest(paymentProviderRequestDto);


RazorPayResponseDto razorPayResponse = razorPayrestClient.post()
        .uri("v1/orders")
        .body(razorPayRequest)
        .retrieve()
        .body(RazorPayResponseDto.class);

        assert razorPayResponse != null;
        PaymentProviderResponseDTO responseDTO = mapToPaymentProviderResponse(razorPayResponse);




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

package com.smartremind.payment_service.payment_provider;

import com.smartremind.payment_service.dto.provider.PaymentProviderRequestDTO;
import com.smartremind.payment_service.dto.provider.PaymentProviderResponseDTO;
import com.smartremind.payment_service.enums.PaymentStatus;

import java.util.UUID;

public  class PaymentProviderImpl implements PaymentProvider{


 private  static  final  String PAYMENT_PROVIDER_ID = "PAYMENT_PROVIDER_ID";


    @Override
    public PaymentProviderResponseDTO processCompletedPayment(PaymentProviderRequestDTO request) {

        String generatedValue = UUID.randomUUID().toString();
        String providerId = PAYMENT_PROVIDER_ID+generatedValue;

        return new PaymentProviderResponseDTO( providerId ,request.paymentId(), PaymentStatus.SUCCESS);

    }

    @Override
    public PaymentProviderResponseDTO processFailurePayment(PaymentProviderRequestDTO request) {



        String generatedValue = UUID.randomUUID().toString();
        String providerId = PAYMENT_PROVIDER_ID+generatedValue;

        return new PaymentProviderResponseDTO( providerId ,request.paymentId(), PaymentStatus.FAILED);

    }
}

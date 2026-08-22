package com.smartremind.payment_service.payment_provider;

import com.smartremind.payment_service.dto.provider.PaymentProviderRequestDTO;
import com.smartremind.payment_service.dto.provider.PaymentProviderResponseDTO;

public interface PaymentProvider {


 PaymentProviderResponseDTO processCompletedPayment(PaymentProviderRequestDTO request);

 PaymentProviderResponseDTO processFailurePayment(PaymentProviderRequestDTO request);



}

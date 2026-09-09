package com.smartremind.payment_service.payment_provider;

import com.smartremind.payment_service.dto.provider.PaymentProviderRequestDto;
import com.smartremind.payment_service.dto.provider.PaymentProviderResponseDTO;

public interface PaymentProvider {


PaymentProviderResponseDTO createOrder(PaymentProviderRequestDto paymentProviderRequestDto);



}

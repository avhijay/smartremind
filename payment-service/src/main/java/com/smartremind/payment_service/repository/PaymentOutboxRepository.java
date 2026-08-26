package com.smartremind.payment_service.repository;

import com.smartremind.payment_service.entity.PaymentOutboxData;
import com.smartremind.payment_service.enums.PaymentOutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentOutboxRepository extends JpaRepository<PaymentOutboxData , Long> {

List<PaymentOutboxData>findByPaymentOutboxStatus(PaymentOutboxStatus status);
Optional<PaymentOutboxData>findByIdempotencyKey(String idempotencyKey);

}

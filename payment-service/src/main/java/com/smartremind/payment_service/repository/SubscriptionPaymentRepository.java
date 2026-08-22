package com.smartremind.payment_service.repository;

import com.smartremind.payment_service.entity.SubscriptionPayment;
import com.smartremind.payment_service.enums.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface SubscriptionPaymentRepository extends JpaRepository<SubscriptionPayment , Long> {

    Optional<SubscriptionPayment>findByPaymentId(String paymentId);
    Optional<SubscriptionPayment>findByUsername(String username);
    Optional<SubscriptionPayment>findByProviderTransactionId(String providerTransactionId);
    Page<SubscriptionPayment>findByPaymentStatus(PaymentStatus status , Pageable pageable);


    boolean existByIdempotencyKey(String idempotencyKey);


    Optional< SubscriptionPayment> findByIdempotencyKey(String idempotencyKey);

    boolean existsByUsername(@NotBlank String username);
}

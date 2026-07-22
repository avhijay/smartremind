package com.smartremind.payment_service.entity;


import com.smartremind.payment_service.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "subscription_table")
@Builder
public class SubscriptionPayment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Setter(AccessLevel.NONE)
    private Long id;


    @Column(name = "payment_id", nullable = false , length = 300 , unique = true , updatable = false)
    private String paymentId;


    @Column(name = "user_name" , nullable = false , length = 250 )
    private String username;



    @Column(name = "subscription_plan_id")
    private  Long subscriptionPlanId;

    @Column(name = "subscription_status", nullable = false)
    @Enumerated (EnumType.STRING)
    private SubscriptionStatus subscriptionStatus;


    @Column(name = "auto_renew" , nullable = false)
    private Boolean autoRenew;

    @Column(name = "renew_count" , nullable = true)
    private Long renewCount;

    @Column(name = "amount" , nullable = false , updatable = false , precision = 10 , scale = 2)
    private BigDecimal amount ;

    @Column(name ="currency" , nullable = false , length = 100)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "payment_method" , nullable = false , length = 100)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "payment_status" , nullable = false , length = 100)
    @Enumerated (EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "retry_count", nullable = false)
    private Integer currentRetryCount  = 0 ;


    @Column(name = "provider_transaction_id"  , length = 250)
    private String providerTransactionId;

    @Column(name = "idempotency_key" , nullable = false , length = 250)
    private String idempotencyKey;


    @Column(name = "paid_at"  )
    private Instant amountPaidAt;


    @Column(name = "activated_at" )
    private Instant activatedAt;

    @Column(name = "expires_at" )
    private Instant expiresAt;

    @Column(name = "cancelled_at" )
    private Instant cancelledAt;

    @Column(name = "created_at"  , nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at" , nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreation(){
        createdAt=Instant.now();
        updatedAt = Instant.now();
    }

@PreUpdate
    void onUpdate(){
        updatedAt = Instant.now();
}










}

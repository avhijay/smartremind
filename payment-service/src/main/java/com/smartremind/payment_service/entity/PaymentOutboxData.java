package com.smartremind.payment_service.entity;


import com.smartremind.payment_service.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "payment_processing_outbox_table")
public class PaymentOutboxData {








    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Setter(AccessLevel.NONE)
    private Long id;


    @Column(name = "payment_id", nullable = false , length = 300 , unique = true , updatable = false)
    private String paymentId;



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
    private PaymentOutboxStatus paymentOutboxStatus;

    @Column(name = "retry_count", nullable = false)
    private Integer currentRetryCount  = 0 ;




    @Column(name = "idempotency_key" , nullable = false , length = 250)
    private String idempotencyKey;







}

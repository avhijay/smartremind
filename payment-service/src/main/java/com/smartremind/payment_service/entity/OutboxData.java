package com.smartremind.payment_service.entity;


import com.smartremind.payment_service.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class OutboxData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "user_name" , nullable = false , length = 250)
    private String userName;

    @Column(name = "unique_id" , nullable = false , unique = true)
    private String uniqueId;

    @Column(name = "subscription_status" , nullable = false )
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus subscriptionStatus;

    @Column(name = "subscription_plan_id" , nullable = false)
    private Long subscriptionId;

    @Column(name = "expires_at" , nullable = false)
    private Instant expiresAt;

    @Column(name = "published" , columnDefinition = "FALSE")
    private  boolean published;




}

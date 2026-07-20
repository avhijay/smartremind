package com.smartremind.payment_service.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "subscription_plans")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class SubscriptionPlans {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "id")
    private Long id;

    @Column(name = "subscription_plan" , nullable = false , length = 150)
    private String subscriptionPlan;

    @Column(name = "amount"  , nullable = false , precision = 10 , scale = 2)

    private BigDecimal amount ;

@Column (name = "plan_duration_days" , nullable = false)
private Integer planDurationDays;

@Column(name = "is_active" ,nullable = false)
private Boolean active;

    @Column(name = "created_at" , nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at" , nullable = false)
    private Instant updatedAt;


@PrePersist
    public void onCreate(){
    createdAt=Instant.now();
    updatedAt = Instant.now();
}

@PreUpdate
    public void onUpdate(){
    updatedAt = Instant.now();
}





}

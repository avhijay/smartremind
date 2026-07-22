package com.smartremind.user_service.entity;

import com.smartremind.user_service.enums.Languages;
import com.smartremind.user_service.enums.SubscriptionStatus;
import com.smartremind.user_service.enums.TimeZone;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "id")
    private Long id;

    @Column(name = "auth_user_name",length = 250 , nullable = false ,unique = true)
    private String userName;

    @Column(name = "email" , length = 300 , nullable = false , unique = true)
 private    String email;

    @Column(name = "first_name",length = 200 , nullable = false)
   private  String firstName;

    @Column(name = "last_name", length = 150, nullable = true)
   private  String lastName;

    @Column(name = "subscription_status" , length = 50 , nullable = false)
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus Subscriptionstatus;


    @Column(name = "subscription_expiry",nullable = true)
    private Instant subscriptionExpiryDate;

    @Column(name = "time_zone", length = 50)
    @Enumerated(EnumType.STRING)
    private TimeZone timeZone;

    @Column(name = "language" , length = 50)
    @Enumerated(EnumType.STRING)
    private Languages languages;

    @Column(name = "email_notifications_enabled")
    private boolean emailNotificationEnabled;


    @Column(name = "sms_notifications_enabled")
    private boolean smsNotificationEnabled;


    @Column(name = "push_notifications_enabled")
    private boolean pushNotificationEnabled;


    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    void onCreate(){
        createdAt=Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    void onUpdate(){
        updatedAt = Instant.now();
    }






}

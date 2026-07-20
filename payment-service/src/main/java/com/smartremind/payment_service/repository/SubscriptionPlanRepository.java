package com.smartremind.payment_service.repository;

import com.smartremind.payment_service.entity.SubscriptionPlans;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlans , Long> {

    List<SubscriptionPlans> findByIsActiveTrue();

}

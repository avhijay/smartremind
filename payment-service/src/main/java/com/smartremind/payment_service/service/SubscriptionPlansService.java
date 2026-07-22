package com.smartremind.payment_service.service;


import com.smartremind.payment_service.dto.ActiveSubscriptionPlansResponseDTO;
import com.smartremind.payment_service.entity.SubscriptionPlans;
import com.smartremind.payment_service.repository.SubscriptionPlanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionPlansService {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionPlansService.class);
    private final SubscriptionPlanRepository subscriptionPlanRepository;


    public SubscriptionPlansService(SubscriptionPlanRepository subscriptionPlanRepository){
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }



    public List<ActiveSubscriptionPlansResponseDTO> getActivePlans(){

      List< SubscriptionPlans> plans =  subscriptionPlanRepository.findByIsActiveTrue();

       List<ActiveSubscriptionPlansResponseDTO> activeSubscriptionPlansResponseDTO = plans.stream().map(this::entityToResponseHelper).toList();
return activeSubscriptionPlansResponseDTO;

    }































    private ActiveSubscriptionPlansResponseDTO entityToResponseHelper(SubscriptionPlans plans){

        return new ActiveSubscriptionPlansResponseDTO
                (
                        plans.getId(), plans.getSubscriptionPlan(),
                plans.getAmount(),plans.getPlanDurationDays(),plans.getActive()

                );

    }






}

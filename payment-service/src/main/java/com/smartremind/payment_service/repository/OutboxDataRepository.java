package com.smartremind.payment_service.repository;

import com.smartremind.payment_service.entity.KafkaOutboxData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutBoxDataRepository extends JpaRepository<KafkaOutboxData, Long> {

    List<KafkaOutboxData> findByPublishedFalse();

}

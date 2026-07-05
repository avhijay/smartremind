package com.smartremind.user_service.repository;

import com.smartremind.user_service.entity.User;
import com.smartremind.user_service.enums.SubscriptionStatus;
import com.smartremind.user_service.projection.UserExpiryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findByStatus(SubscriptionStatus status , Pageable pageable);

    Optional<UserExpiryProjection>findProjectionById(Long id);

    Optional<User>findByUserName(String userName);

    Optional<User>findByEmail(String email);

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

}

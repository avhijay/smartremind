[1mdiff --git a/payment-service/src/main/java/com/smartremind/payment_service/repository/SubscriptionPaymentRepository.java b/payment-service/src/main/java/com/smartremind/payment_service/repository/SubscriptionPaymentRepository.java[m
[1mindex 850cb02..fdf842e 100644[m
[1m--- a/payment-service/src/main/java/com/smartremind/payment_service/repository/SubscriptionPaymentRepository.java[m
[1m+++ b/payment-service/src/main/java/com/smartremind/payment_service/repository/SubscriptionPaymentRepository.java[m
[36m@@ -15,5 +15,10 @@[m [mpublic interface SubscriptionPaymentRepository extends JpaRepository<Subscriptio[m
     Optional<SubscriptionPayment>findByProviderTransactionId(String providerTransactionId);[m
     Page<SubscriptionPayment>findByPaymentStatus(PaymentStatus status , Pageable pageable);[m
 [m
[32m+[m[32m    Optional<SubscriptionPayment>findByIdempotencyKey(String idempotencyKey);[m
[32m+[m
[32m+[m[32m    boolean existsByUsername(String username);[m
[32m+[m[32m    boolean existByIdempotencyKey(String idempotencyKey);[m
[32m+[m
 [m
 }[m

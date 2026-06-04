package com.smartremind.user_service.projection;

import java.time.Instant;

public interface UserExpiryProjection {

    Instant getSubscriptionExpiryDate();
}

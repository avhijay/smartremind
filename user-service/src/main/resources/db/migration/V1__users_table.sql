CREATE TABLE users(
id BIGSERIAL PRIMARY KEY,
auth_user_name VARCHAR(250) UNIQUE NOT NULL ,
email VARCHAR(300) UNIQUE NOT NULL,
first_name VARCHAR(200) NOT NULL,
last_name VARCHAR(150) ,
subscription_status VARCHAR(50) NOT NULL DEFAULT 'FREE',
subscription_expiry TIMESTAMP ,
CONSTRAINT expiry_subscription_value
CHECK(
subscription_status <> 'PREMIUM'
OR
subscription_expiry IS NOT NULL
),
time_zone VARCHAR(50) DEFAULT 'IST',
language VARCHAR(50) DEFAULT 'ENGLISH',

email_notifications_enabled BOOLEAN DEFAULT TRUE,
sms_notifications_enabled BOOLEAN DEFAULT FALSE,
push_notifications_enabled BOOLEAN DEFAULT TRUE,

created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP

);

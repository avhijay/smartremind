
CREATE TABLE payment_processing_outbox_table(

id BIGSERIAL PRIMARY KEY,
payment_id VARCHAR(300) UNIQUE NOT NULL,


amount NUMERIC(10,2),
currency VARCHAR(100) NOT NULL DEFAULT 'INR',
payment_method VARCHAR (100) NOT NULL,
payment_status VARCHAR (100) NOT NULL DEFAULT 'PENDING',
retry_count BIGINT DEFAULT 0 ,
subscription_plan_id BIGINT NOT NULL,
idempotency_key VARCHAR(250) UNIQUE NOT NULL,


);
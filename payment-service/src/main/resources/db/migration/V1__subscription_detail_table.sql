CREATE TABLE subscription_table(

id BIGSERIAL PRIMARY KEY,
payment_id VARCHAR(300) UNIQUE NOT NULL,
user_name VARCHAR(250) NOT NULL,
subscription_plan VARCHAR(150),
subscription_status VARCHAR(150) NOT NULL,
auto_renew BOOLEAN  DEFAULT 'TRUE' ,

renew_count BIGSERIAL ,
amount NUMERIC(10,2),
currency VARCHAR(100) NOT NULL DEFAULT 'INR',
payment_method VARCHAR (100) NOT NULL,
payment_status VARCHAR (100) NOT NULL DEFAULT 'PENDING',
retry_count BIGINT  ,
provider_transaction_id VARCHAR(250) NOT NULL,
idempotency_key VARCHAR(250) UNIQUE NOT NULL,
paid_at TIMESTAMP NOT NULL,
activated_at TIMESTAMP NOT NULL,
expires_at TIMESTAMP  NOT NULL,
cancelled_at TIMESTAMP  NOT NULL,
created_at TIMESTAMP NOT NULL ,
updated_at TIMESTAMP NOT NULL








);
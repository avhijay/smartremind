CREATE TABLE subscription_table(

id BIGSERIAL PRIMARY KEY,
payment_id VARCHAR(300) UNIQUE NOT NULL,
user_name VARCHAR(250) NOT NULL,

subscription_plan_id BIGSERIAL NOT NULL,
subscription_status VARCHAR(150) NOT NULL,
auto_renew BOOLEAN  DEFAULT 'TRUE' ,

renew_count BIGSERIAL ,
amount NUMERIC(10,2),
currency VARCHAR(100) NOT NULL DEFAULT 'INR',
payment_method VARCHAR (100) NOT NULL,
payment_status VARCHAR (100) NOT NULL DEFAULT 'PENDING',
retry_count BIGINT DEFAULT 0 ,
provider_transaction_id VARCHAR(250) ,
idempotency_key VARCHAR(250) UNIQUE NOT NULL,
paid_at TIMESTAMP ,
activated_at TIMESTAMP ,
expires_at TIMESTAMP  ,
cancelled_at TIMESTAMP  ,
created_at TIMESTAMP NOT NULL ,
updated_at TIMESTAMP NOT NULL








);
CREATE TABLE kafka_outbox_table(

id BIGSERIAL PRIMARY KEY ,
user_name VARCHAR(250) NOT NULL,
unique_id VARCHAR(300) UNIQUE NOT NULL,
subscription_status VARCHAR(150) NOT NULL,
subscription_plan_id BIGSERIAL NOT NULL,
expires_at TIMESTAMP  NOT NULL ,
published BOOLEAN DEFAULT'FALSE'

);
CREATE table subscription_plans(

id BIGSERIAL PRIMARY KEY ,
subscription_plan VARCHAR(150) NOT NULL UNIQUE,
amount NUMERIC(10,2),
plan_duration_days INTEGER NOT NULL,
is_active BOOLEAN NOT NULL,
created_at TIMESTAMP NOT NULL,
updated_at TIMESTAMP NOT NULL


);
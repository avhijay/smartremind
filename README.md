# SmartRemind

A **backend-focused microservices application** built with **Spring Boot** to explore production-style distributed system design. 
The project emphasizes **service ownership**, **centralized authentication**, **secure inter-service communication**, and
**event-driven architecture** over traditional CRUD application development.

The objective is to understand the architectural decisions behind modern backend systems and 
implement them in a practical environment while keeping each service focused on a single responsibility.

---

# Architecture

The system is organized as independently deployable services.

## Current Services

* **API Gateway**
* **Authorization Service**
* **User Service**
* **Payment Service**
* **Service Registry (Eureka)**
* **Common Library**

## Planned Services

* **Reminder Service**
* **Scheduler Service**
* **Notification Service**

Every external request enters through the **API Gateway**. Internal services are never exposed directly to clients, allowing routing, authentication, and
request validation to remain centralized.

---

# Design Decisions

## Centralized Authentication

One of the primary goals of this project was to eliminate duplicated authentication logic across microservices.

Instead of allowing every service to validate JWTs independently, authentication is centralized inside the **Authorization Service**,
while the **API Gateway** becomes the single entry point into the system.

The gateway:

* Validates JWT access tokens
* Retrieves public keys through the JWK Set endpoint
* Extracts authenticated user information
* Forwards trusted identity headers to downstream services
* Routes requests through Eureka Service Discovery

This keeps business services focused entirely on domain logic instead of security configuration.

---

## API Gateway

The gateway is responsible for concerns that should remain common across the system.

Current responsibilities include:

* **JWT validation**
* **Spring Security integration**
* **Service routing**
* **Rate limiting**
* **Request forwarding**
* **Identity propagation**
* **Service discovery integration**

Downstream services trust requests that have already been authenticated by the gateway instead of performing the same work repeatedly.

---

## Service Ownership

Each microservice owns exactly one business responsibility.

* **Authorization Service** owns authentication.
* **User Service** owns user information.
* **Payment Service** owns subscriptions and payment records.

Business data is never intentionally owned by multiple services simultaneously.

This keeps responsibilities well-defined and reduces unnecessary coupling between services.

---

## Event-Driven Communication

The project adopts **Apache Kafka** for communication between services whenever direct synchronous communication would unnecessarily couple two business domains.

### User Registration

When a new account is created:

1. The **Authorization Service** creates the account credentials.
2. A **User Created** event is published.
3. **Kafka** delivers the event.
4. The **User Service** creates the user's profile.

The Authorization Service never writes directly into the User Service database.

---

### Subscription Updates

After a successful subscription purchase:

1. The **Payment Service** validates the purchase.
2. The payment is persisted.
3. A **Subscription Purchased** event is published.
4. **Kafka** delivers the event.
5. The **User Service** updates subscription information.

The Payment Service never modifies user records directly.

The User Service remains the **single source of truth** for user-related information.

---

## Payment Service

The Payment Service is responsible for managing subscriptions independently from user management.

Current implementation includes:

* Subscription plan management
* Payment request handling
* Payment persistence
* Flyway database migrations
* Development UI for backend integration

The included HTML, CSS and JavaScript interface exists solely for interacting with backend APIs during development. 
The project remains **backend-first**, and the frontend is intentionally lightweight.

Planned improvements include:

* Idempotent payment requests
* Kafka event publishing
* Subscription lifecycle management
* Improved failure handling

---

## Database Versioning

Database schema evolution is managed using **Flyway**.

Every structural database change is introduced through versioned migration scripts rather than manual SQL updates.

This ensures that every developer and every deployment environment evolves the database consistently.

---

## Docker Compose

The project is designed to run as a complete local distributed environment.

**Docker Compose** orchestrates infrastructure components and services from a single configuration, 
allowing the entire application stack to be started consistently without manual setup.

---

## Thread Safety & Reliability

Several components are intentionally designed with concurrent request processing in mind.

Current and planned implementations focus on:

* Preventing duplicate payment processing through **idempotency**
* Maintaining transactional consistency
* Asynchronous communication through Kafka
* Clear service ownership
* Centralized authentication
* Reliable request routing

---

## Future Improvements

The project continues to evolve toward a production-style distributed backend.

Planned work includes:

* **Apache Kafka** integration
* **Quartz Scheduler**
* **Reminder Service**
* **Notification Service**
* **Circuit Breaker (Resilience4j)**
* **Redis** caching and distributed rate limiting
* **Prometheus** and **Grafana** for observability
* **Zipkin** distributed tracing
* End-to-end integration testing

---

# Current Progress

## Implemented

* API Gateway
* OAuth2 Authorization Server
* Spring Security
* JWT Authentication
* JWK-based token validation
* Header forwarding through the gateway
* Eureka Service Discovery
* Flyway database migrations
* Payment Service
* Subscription plan management
* Development payment interface
* Docker Compose environment

## In Progress

* Kafka event publishing
* User registration events
* Subscription synchronization

---

# Running the Project

Clone the repository.

Start the required infrastructure using **Docker Compose**.

Start individual Spring Boot services from their respective modules.

The project is under active development, and additional infrastructure components will be integrated as new services are introduced.

---

# Project Goal

SmartRemind is intended to explore **how modern backend systems are designed**, not simply how REST APIs are implemented.

The project focuses on understanding **service boundaries**, **centralized authentication**, 
**secure inter-service communication**, **event-driven workflows**, **database versioning**, **distributed infrastructure**,
and the architectural decisions commonly found in production microservices.

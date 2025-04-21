# Project Context: e-Shop Microservices

This document provides a comprehensive overview of the e-Shop microservices ecommerce project, covering the full SDLC lifecycle, including architecture, networking, performance, availability, deployment, async/sync communication, monitoring, logging, and pipelines.

---

## 1. Architecture Overview

- **Microservices:**
  - `inventory-service`: Manages product inventory.
  - `order-service`: Handles order placement and management.
  - `product-service`: Manages product catalog.
  - `notification-service`: Sends notifications (likely async/events).
  - `api-gateway`: Entry point for client requests; routes to services.
  - `discovery-registry`: Service discovery (likely Eureka or similar).

- **Supporting Services:**
  - `keycloak`: Authentication/authorization (OAuth2, OIDC).
  - `Prometheus` & `Grafana`: Monitoring and dashboards.
  - `Zipkin`: Distributed tracing.
  - `Kafka`: Messaging/event streaming (async communication).
  - `MySQL`/`MongoDB`: Databases for various services.

- **Orchestration:**
  - Managed via `docker-compose.yml` for local development and integration.

- **Build/Dependency Management:**
  - Java 17, Spring Boot 3.2.3, Spring Cloud 2023.0.0, Maven (multi-module).

---

## 2. Service Details

Each microservice is a Spring Boot application with its own `pom.xml` and `src` directory.

- **API Gateway:**
  - Routes external requests to internal services.
  - Handles cross-cutting concerns (rate-limiting, auth, etc.).

- **Discovery Registry:**
  - Central registry for service discovery (Spring Cloud/Eureka).

- **Inventory/Order/Product Services:**
  - Expose REST APIs for CRUD operations.
  - Use MySQL/MongoDB for persistence.
  - Register with discovery service.
  - Likely communicate via REST (sync) and Kafka (async/events).

- **Notification Service:**
  - Listens to Kafka topics for events (order placed, etc.).
  - Sends notifications (email, SMS, etc.).

- **Keycloak:**
  - Manages authentication/authorization.
  - Realm config imported at startup.

---

## 3. Networking & Communication

- **Service Discovery:**
  - All services register with discovery-registry (Eureka).
  - API Gateway uses discovery to route requests.

- **Sync Communication:**
  - REST APIs between services (Spring MVC/Web).

- **Async Communication:**
  - Kafka for event-driven patterns (order events, notifications).

- **Databases:**
  - MySQL for order/inventory/keycloak data.
  - MongoDB for other data (e.g., products).

---

## 4. Performance & Availability

- **Scaling:**
  - Each service is independently deployable and scalable.
  - Stateless services (except for DB dependencies).

- **Resilience:**
  - Health checks defined in Docker Compose.
  - Restart policies for containers.
  - Service discovery enables failover.

- **Caching:**
  - Not explicitly found, but can be added via Spring Cache/Redis.

---

## 5. Deployment

- **Local:**
  - `docker-compose.yml` spins up all services, databases, brokers, and monitoring tools.

- **Containerization:**
  - Each service has a Dockerfile (or uses Jib Maven plugin for image builds).

- **CI/CD:**
  - Jib Maven plugin for building/pushing images.
  - No explicit pipeline scripts found, but project is CI/CD ready.

---

## 6. Monitoring, Logging, and Tracing

**Recommended Observability Stack:**

- **Prometheus + Grafana (metrics & dashboards):**
  - Prometheus scrapes metrics from each microservice (via `/actuator/prometheus` endpoint exposed by Spring Boot Actuator).
  - Grafana visualizes these metrics and provides dashboards for system and application health.
  - Prometheus is configured via `Prometheus/Prometheus.yml`; dashboards are managed in `Grafana-Dashboard.json`.

- **Zipkin (tracing):**
  - Distributed tracing is enabled across all services using Spring Cloud Sleuth.
  - Sleuth auto-generates and propagates trace IDs for all requests.
  - Traces are sent to Zipkin, which provides a UI for visualizing request flows and diagnosing latency/bottlenecks.

- **ELK Stack (Elasticsearch, Logstash, Kibana) or Loki+Grafana (logs):**
  - Centralized log aggregation is achieved using either the ELK stack or Loki (integrated with Grafana).
  - Log shippers (Filebeat for ELK, Promtail for Loki) collect logs from all containers/services.
  - Kibana or Grafana is used for searching, visualizing, and alerting on logs.

- **Spring Boot Actuator & Sleuth in each service:**
  - Spring Boot Actuator exposes health, metrics, and info endpoints for monitoring.
  - Spring Cloud Sleuth enables distributed tracing and integrates with Zipkin.

**Integration Points:**
- All microservices must include Spring Boot Actuator and Sleuth dependencies.
- Prometheus scrapes metrics endpoints; Grafana consumes Prometheus data.
- Services send trace data to Zipkin.
- Logs are shipped to ELK or Loki, then visualized in Kibana or Grafana.

This stack ensures full visibility into metrics, logs, and traces for monitoring, troubleshooting, and alerting across your microservices ecosystem.

---

## 7. Security

- **Keycloak:**
  - Centralized authentication/authorization.
  - Realm imported at startup.
  - Services integrate with Keycloak for OAuth2/OIDC.

---

## 8. Pipelines & Automation

- **Build:**
  - Maven multi-module project.
  - Jib Maven plugin for container builds.

- **Deploy:**
  - Docker Compose for local/dev.
  - Images can be pushed to Docker Hub or other registries.

- **CI/CD:**
  - Ready for integration with Jenkins, GitHub Actions, GitLab CI, etc.

---

## 9. Observability & Operations

- **Health Checks:**
  - Defined in Docker Compose; Spring Boot actuators can be used.

- **Tracing:**
  - Zipkin collects and visualizes traces.

- **Metrics:**
  - Prometheus scrapes, Grafana visualizes.

---

## 10. Extensibility

- New services can be added by duplicating a service module, adding to `pom.xml` and `docker-compose.yml`.
- Event-driven patterns can be expanded via Kafka.
- Security policies managed centrally via Keycloak.

---

## 11. References

- `docker-compose.yml`: Service orchestration, networking, environment configs.
- `pom.xml`: Dependency management, build plugins, modules.
- `Prometheus/Prometheus.yml`: Monitoring targets.
- `Grafana-Dashboard.json`: Dashboard config.
- `keycloakRealm/realms`: Security/realm config.

---

This context file should serve as a comprehensive reference for onboarding, development, operations, and future enhancements.

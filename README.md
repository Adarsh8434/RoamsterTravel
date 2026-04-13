# Roamster – Backend Microservices

Module owner: **Clothing / Food / Photo / E-commerce**

---

## Services in this module

| Service | Port | Responsibility |
|---|---|---|
| `clothing-service` | 8081 | Clothing recommendations + rent/buy orders |
| `food-service` | 8082 | Food recommendations |
| `photo-service` | 8083 | Best-shot / pose recommendations |
| `ecommerce-service` | 8084 | Central order management + merchant registry |

---

## Architecture

```
Mobile App
    │
    ▼
API Gateway (other team)
    │
    ├──► clothing-service  ──┐
    ├──► food-service        ├──► PostgreSQL (4 separate DBs)
    ├──► photo-service       │
    └──► ecommerce-service ──┘
              │
              ▼
         RabbitMQ (roamster.events exchange)
              │
    ┌─────────┴──────────┐
    │  clothing.order.#  │  → consumed by ecommerce-service
    │  clothing.rec.gen  │  → consumed by ML pipeline (other team)
    │  food.rec.gen      │
    │  photo.rec.gen     │
    └────────────────────┘
```

### Communication pattern
- **REST (Feign)** – synchronous calls between services where immediate response is needed
  - `ecommerce-service` → `clothing-service` (order lookup)
- **RabbitMQ** – async events for order lifecycle, recommendation generation signals

---

## Tech stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2 |
| ORM | Spring Data JPA / Hibernate |
| Database | PostgreSQL 16 |
| Migrations | Flyway |
| Messaging | RabbitMQ 3.13 (AMQP) |
| REST clients | OpenFeign |
| API docs | SpringDoc OpenAPI (Swagger UI) |
| Containers | Docker + Docker Compose |

---

## Project structure

```
roamster/
├── clothing-service/
│   └── src/main/java/com/roamster/clothing/
│       ├── controller/   ClothingController.java
│       ├── service/      ClothingService.java, ClothingRuleEngine.java
│       ├── repository/   RecommendationRepository.java, ClothingOrderRepository.java
│       ├── model/        Recommendation.java, ClothingRecommendationDetail.java, ClothingOrder.java
│       ├── dto/          ClothingDTOs.java
│       ├── messaging/    ClothingEventPublisher.java
│       └── config/       RabbitMQConfig.java
│   └── src/main/resources/
│       ├── application.yml
│       └── db/migration/V1__init_clothing_schema.sql
│
├── food-service/         (same pattern, port 8082)
├── photo-service/        (same pattern, port 8083)
├── ecommerce-service/    (same pattern, port 8084)
│
├── docker/
│   └── init-multiple-dbs.sh
├── docker-compose.yml
├── openapi.yml           ← Full API contract for all 4 services
└── README.md
```

---

## Running locally

### Prerequisites
- Docker Desktop installed and running
- No processes on ports 5432, 5672, 15672, 8081–8084

### 1. Start everything with Docker Compose

```bash
cd roamster/
docker compose up --build
```

This will:
1. Start PostgreSQL and create 4 databases automatically
2. Start RabbitMQ
3. Build and start all 4 Spring Boot services
4. Run Flyway migrations on each service startup

### 2. Verify services are up

```bash
# Health checks
curl http://localhost:8081/actuator/health   # clothing
curl http://localhost:8082/actuator/health   # food
curl http://localhost:8083/actuator/health   # photo
curl http://localhost:8084/actuator/health   # ecommerce
```

### 3. Access Swagger UI

| Service | URL |
|---|---|
| Clothing | http://localhost:8081/swagger-ui.html |
| Food | http://localhost:8082/swagger-ui.html |
| Photo | http://localhost:8083/swagger-ui.html |
| E-commerce | http://localhost:8084/swagger-ui.html |

### 4. RabbitMQ Management UI
http://localhost:15672 → guest / guest

---

## Quick API test (curl)

```bash
# Generate a clothing recommendation
curl -X POST http://localhost:8081/api/v1/clothing/recommendations \
  -H "Content-Type: application/json" \
  -d '{
    "tripId": 101, "userId": 42,
    "destination": "Goa", "weather": "HOT",
    "travelStyle": "AESTHETIC", "activityIntensity": "LOW",
    "socialMediaGoal": true, "travelType": "COUPLE"
  }'

# Generate a food recommendation
curl -X POST http://localhost:8082/api/v1/food/recommendations \
  -H "Content-Type: application/json" \
  -d '{
    "tripId": 101, "userId": 42,
    "dietaryPreference": "VEG", "timeOfDay": "AFTERNOON",
    "spicePreference": "MEDIUM", "travelType": "FAMILY"
  }'

# Generate a photo/best-shot recommendation
curl -X POST http://localhost:8083/api/v1/photo/recommendations \
  -H "Content-Type: application/json" \
  -d '{
    "tripId": 101, "userId": 42,
    "locationType": "WATERFRONT", "timeOfDay": "EVENING",
    "travelStyle": "AESTHETIC", "socialMediaGoal": true
  }'

# Place a clothing RENT order
curl -X POST http://localhost:8081/api/v1/clothing/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 42, "tripId": 101, "merchantId": 7,
    "recommendationId": 1, "orderType": "RENT",
    "totalAmount": 499.00,
    "rentalStartDate": "2026-04-10T10:00:00",
    "rentalEndDate": "2026-04-15T10:00:00"
  }'
```

---

## RabbitMQ event routing

| Routing key | Publisher | Consumer |
|---|---|---|
| `clothing.recommendation.generated` | clothing-service | ML pipeline |
| `clothing.order.placed` | clothing-service | ecommerce-service |
| `clothing.order.status.updated` | clothing-service | notification-service |
| `food.recommendation.generated` | food-service | ML pipeline |
| `photo.recommendation.generated` | photo-service | ML pipeline |
| `ecommerce.order.created` | ecommerce-service | notification-service |
| `ecommerce.order.status.changed` | ecommerce-service | notification-service |

---

## Environment variables reference

| Variable | Default | Description |
|---|---|---|
| `DB_HOST` | localhost | PostgreSQL host |
| `DB_PORT` | 5432 | PostgreSQL port |
| `DB_NAME` | roamster_* | Database name per service |
| `DB_USER` | roamster | DB username |
| `DB_PASS` | roamster | DB password |
| `RABBITMQ_HOST` | localhost | RabbitMQ host |
| `RABBITMQ_PORT` | 5672 | RabbitMQ AMQP port |
| `RABBITMQ_USER` | guest | RabbitMQ username |
| `RABBITMQ_PASS` | guest | RabbitMQ password |

---

## Next steps / integration points

- **ML Service** (other team) – subscribe to `*.recommendation.generated` events to improve model
- **Notification Service** (other team) – subscribe to `*.order.*` events to send push/email/SMS
- **Payment Service** (other team) – `paymentId` FK in orders to be linked after payment confirmation
- **User/Trip Service** (other team) – `userId` and `tripId` validated via REST call (add Feign client)
- Replace `System.currentTimeMillis()` stubs with actual FK to shared `recommendations` table once DB is unified or an API contract is agreed with the recommendation team

Made by Adarsh kumar choubey

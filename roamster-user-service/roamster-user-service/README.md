# Roamster — User Service (Spring Boot)

Microservice for **Authentication, Authorization, Profile & Preferences**.

## Stack: Spring Boot 3.2 · Java 17 · PostgreSQL 16 · Flyway · JWT · Docker

## Quick Start
```bash
export JWT_SECRET=your-secret-key-min-32-characters
docker compose up --build
curl http://localhost:8001/actuator/health
```

## API
| Method | URL | Auth | Description |
|--------|-----|------|-------------|
| POST | `/api/v1/auth/register` | - | Register user |
| POST | `/api/v1/auth/login` | - | Login → JWT |
| POST | `/api/v1/auth/refresh` | - | Refresh tokens |
| GET | `/api/v1/profile/me` | Bearer | Get profile |
| PATCH | `/api/v1/profile/me` | Bearer | Update profile |
| GET | `/api/v1/preferences/me` | Bearer | Get preferences |
| PATCH | `/api/v1/preferences/me` | Bearer | Update preferences |
| PATCH | `/api/v1/admin/users/{id}/activate` | ADMIN | Activate user |
| PATCH | `/api/v1/admin/users/{id}/role` | ADMIN | Change role |

## Notes
- New accounts are **inactive** by default; admin must activate before first login
- Runs on port **8001**, Docker network: `roamster-network`
- Flyway handles all DB migrations automatically on startup

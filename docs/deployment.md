## Deployment Guide

### 1. Prerequisites

- JDK 17
- Maven 3.9+
- MySQL 8.x (database `digital_governance`)
- Redis (optional) if you want background AI queueing
- Node/npm (optional) for bundling frontend assets

### 2. Environment Variables

Create `backend/.env` or export variables:

```
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/digital_governance
SPRING_DATASOURCE_USERNAME=gov_user
SPRING_DATASOURCE_PASSWORD=secret
APP_SECURITY_JWT_SECRET=change-me
APP_SECURITY_JWT_EXPIRATION-MS=86400000
```

### 3. Database Bootstrap

```sql
CREATE DATABASE digital_governance;
CREATE USER 'gov_user'@'%' IDENTIFIED BY 'secret';
GRANT ALL ON digital_governance.* TO 'gov_user'@'%';
```

Spring JPA auto-migrates tables via `hibernate.ddl-auto=update`. Use Flyway for production.

### 4. Backend Build & Run

```bash
cd backend
mvn clean package
java -jar target/digital-governance-platform-1.0.0.jar
```

### 5. Frontend Hosting

Serve the `frontend/` folder via any static host:

```bash
npx serve ../frontend --port 4173
```

Configure CORS/HTTPS in reverse proxy (Nginx/IIS) pointing `/api` to Spring Boot and `/` to static UI.

### 6. Docker (Optional)

Use the provided `Dockerfile` template (create one referencing `backend/target/*.jar`). Run `docker compose up` with services for app, MySQL, Redis.

### 7. Observability

- Health: `GET /actuator/health`
- Metrics: `GET /actuator/metrics`
- Logs: Use ELK / Azure Monitor with JSON layout.


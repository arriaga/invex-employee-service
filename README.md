# Employee Service

## Local setup

### Start MySQL
```bash
docker compose up -d
```

### Run the application with dev profile
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Run tests
```bash
mvn clean test
```

### Verify health
```bash
curl http://localhost:8080/actuator/health
```

## API documentation

Swagger UI is available once the application is running:

```bash
http://localhost:8080/swagger-ui/index.html
```

OpenAPI JSON:

```bash
http://localhost:8080/v3/api-docs
```

## Security

This service uses JWT Bearer tokens as a Resource Server.

Public endpoints:
- GET /actuator/health
- GET /v3/api-docs/**
- GET /swagger-ui/**

Protected endpoints:
- GET /employees, GET /employees/{id}, GET /employees/search require SCOPE_employee.read
- POST /employees, PUT /employees/{id}, DELETE /employees/{id} require SCOPE_employee.write

### Local JWT generation

Local validation uses an HMAC secret configured by `JWT_SECRET` (defaults to `dev-local-secret-change-me`).

Example token generation (HS256) using Python:

```bash
python - <<'PY'
import jwt
import datetime

secret = "dev-local-secret-change-me"
now = datetime.datetime.utcnow()
payload = {
    "sub": "local-user",
    "scope": "employee.read employee.write",
    "iat": now,
    "exp": now + datetime.timedelta(hours=1)
}
print(jwt.encode(payload, secret, algorithm="HS256"))
PY
```

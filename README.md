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

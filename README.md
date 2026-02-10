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

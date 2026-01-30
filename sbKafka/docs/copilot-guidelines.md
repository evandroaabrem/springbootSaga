# Copilot Guidelines

# Copilot Guidelines

## Java
- Java 17+
- Use records for DTOs
- Constructor injection only
- No field injection
- Generate JavaDocs for public methods

## Packages (Authoritative)
- Model classes must be in package src/main/java/com/example/sbkafka/model
- Repository interfaces must be in package src/main/java/com/example/sbkafka/repository
- Service classes must be in package src/main/java/com/example/sbkafka/service
- REST controllers must be in package src/main/java/com/example/sbkafka/controller
- DTOs must be in package src/main/java/com/example/sbkafka/dto

## Create CRUD
- Create model class in package src/main/java/com/example/sbkafka/model
- Create repository interface in package src/main/java/com/example/sbkafka/repository
- Create service class in package src/main/java/com/example/sbkafka/service
- Create REST controller class in package src/main/java/com/example/sbkafka/controller
- Create DTOs using Java records (request/response)
- Add Lombok annotations where appropriate
- Generate mapper class to convert model <-> DTO

## Spring
- @Transactional only in service layer
- Controllers return DTOs, never entities
- Repositories are interfaces
- Prefer constructor injection

## SOLID
- One responsibility per class
- Prefer composition over inheritance
- No God classes

## Testing
- JUnit 5
- Mockito
- One behavior per test
- Descriptive test names
# GEMINI.md

## Project
- A real estate review platform for people searching for pet-friendly housing.

## Context
- Spring Boot 3.x API service
- Java 21, JPA (Hibernate), Querydsl, PostgreSQL, Spring Security, Junit, Assertj
- Users can search and review buildings 
- Monolithic, layered architecture (Controller → Service → Domain → Infrastructure)

## Standards
- Always follow Test Driven Development (TDD)
- Use Testcontainers when tests require external dependencies
- Integration tests that require RDB or Redis dependencies must extend IntegrationTestSupport.
- Prefer integration tests except for pure domain logic, which should be covered by unit tests
- Follow RESTful API conventions
- Apply SOLID principles and DIP
- Prefer domain-driven design
- Avoid exposing technical details in error messages
- Write clean, intention-revealing code
- Use Java record for DTO classes
- Use '@MockitoBean' for mocking Spring beans in tests.
- Do not include logging statements in test assertions.
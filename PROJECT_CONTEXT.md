You are assisting in building a production-style backend system using Java and Spring Boot.

PROJECT OVERVIEW:
We are building a transactional digital banking backend service. The system allows authenticated users to create accounts and safely transfer funds between accounts while ensuring transactional integrity and preventing race conditions or double spending.

ARCHITECTURE:
Use a simple layered architecture:
- controller (thin controllers, no business logic)
- service (all business logic here)
- repository (Spring Data JPA interfaces)
- entity (JPA entities only)
- dto (request and response DTOs)
- security (JWT-based authentication)
- config (configuration classes)
- exception (global exception handling using @ControllerAdvice)

DESIGN PRINCIPLES:
1. Controllers must only handle HTTP requests and delegate to services.
2. All business logic must be inside the service layer.
3. Use DTOs for request/response. Never expose entities directly.
4. Use consistent naming conventions.
5. Use proper formatting and clean code style.
6. Add meaningful JavaDoc comments for important methods.
7. Keep indentation consistent (4 spaces).
8. Avoid unnecessary comments, but document complex logic clearly.
9. Follow REST API best practices.
10. Prepare the system to support transactional fund transfers using @Transactional.

CORE FEATURES TO IMPLEMENT:
- User registration and login (JWT authentication)
- Account creation
- Balance check
- Fund transfer between accounts
- Transaction history
- Idempotency support for transfer API
- Pessimistic locking for concurrency safety
- Global exception handling
- Standard API response format

DATABASE:
Use PostgreSQL with JPA/Hibernate.
Tables:
- users
- accounts
- transactions

NON-FUNCTIONAL GOALS:
- Ensure ACID compliance for fund transfer
- Prevent double spending
- Maintain clean layered architecture
- Use proper error handling
- Write code that is interview-ready and production-style

IMPORTANT:
Keep formatting consistent throughout the project.
Use clear method names.
Use constructor injection.
Avoid field injection.
Follow clean coding standards.

Generate structured, clean, maintainable, and interview-quality code.
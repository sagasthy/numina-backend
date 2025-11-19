# Numina Backend - AI Coding Instructions

## Project Overview
Numina is a **Personal Finance Management (PFM) Platform** built with Spring Boot 3.5.7 and Java 25. It enables users to track income/expenses, manage multiple accounts, categorize transactions, generate financial reports, and set savings goals.

## Architecture & Structure

### Tech Stack
- **Framework**: Spring Boot 3.5.7 with Spring Security, JPA, and Validation
- **Database**: MySQL 9.4 with Flyway migrations
- **Authentication**: JWT-based (using jjwt 0.11.5)
- **API Documentation**: SpringDoc OpenAPI 2.3.0 (Swagger UI at `/swagger-ui.html`)
- **Build**: Maven with Java 25
- **Deployment**: Docker Compose (app + MySQL)

### Package Structure
```
com.kar.numina/
├── config/          # Spring configuration (Security, Flyway, OpenAPI, Dotenv)
├── controller/      # REST endpoints (e.g., UserAuthController)
├── entity/          # JPA entities (e.g., User.java)
├── model/           # DTOs and response wrappers
├── repository/      # Spring Data JPA repositories
├── service/         # Business logic layer
└── util/            # Utilities (e.g., JwtUtil)
```

### Database Schema
Core entities: `users`, `accounts`, `categories`, `transactions`, `goals`, `audit_logs`. See `SchemaDesign.md` for complete ER diagram. Migrations in `src/main/resources/db/migration/` follow Flyway naming: `V{version}__{description}.sql`.

## Key Patterns & Conventions

### Response Wrapping
All API responses use standardized wrappers:
- **Success**: `SuccessResponse<T>` with `data`, `message`, `status`
- **Error**: `ErrorResponse` with `message`, `statusCode`

Example from `UserAuthController`:
```java
return ResponseEntity.ok()
    .body(SuccessResponse.<User>builder()
        .message("User retrieved successfully")
        .data(user)
        .status(HttpStatus.OK.value())
        .build());
```

### Entity Design
- Use **Lombok** annotations: `@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`
- JPA entities in `entity/` package, DTOs in `model/` package
- Timestamps: `@CreationTimestamp` and `@UpdateTimestamp` from Hibernate
- Example: `User.java` entity with `userId` as `@GeneratedValue(strategy = GenerationType.IDENTITY)`

### Security Configuration
- Public endpoints: `/auth/**`, `/v3/api-docs/**`, `/swagger-ui/**`
- All other endpoints require authentication
- Password encoding: BCrypt
- CSRF disabled (JWT-based stateless auth)
- See `SecurityConfig.java` for Spring Security filter chain

### Environment Variables
Uses **custom Dotenv loader** (`DotenvEnvironmentPostProcessor`):
- Profile-specific files: `.env.development`, `.env.docker`
- Loads via `META-INF/spring.factories`
- Database credentials: `MYSQL_DATABASE`, `MYSQL_USER`, `MYSQL_PASSWORD`

## Developer Workflows

### Local Development
1. **Start MySQL**: `docker-compose up db -d` (runs on port 3306)
2. **Run application**: 
   - IDE: Run `NuminaBackendApplication.java`
   - Terminal: `./mvnw spring-boot:run`
   - Profile: `spring.profiles.active=development` (default)
3. **Access Swagger UI**: http://localhost:8080/swagger-ui.html

### Docker Deployment
```bash
docker-compose up --build
```
- App waits 30s for DB health check (see Dockerfile `CMD`)
- Uses `application-docker.yml` profile with `SPRING_PROFILES_ACTIVE=docker`

### Database Migrations
- **Create migration**: Add `V{x}_{y}_{z}__{Description}_YYYYMMDD.sql` in `src/main/resources/db/migration/`
- **Apply**: Automatic on app startup (Flyway)
- **Retry config**: 10 retries, 5s interval (`spring.flyway.connect-retries`)

### Testing
- **Test class location**: `src/test/java/com/kar/numina/`
- **DO NOT auto-generate step definitions** (see `.github/instructions/test-generation.instructions.md`)
- Run tests: `./mvnw test`

## Project-Specific Conventions

### Naming
- **Entities**: Singular, PascalCase (e.g., `User.java`)
- **Tables**: Plural, snake_case (e.g., `users`)
- **Endpoints**: RESTful conventions (e.g., `/auth/register`, `/auth/user/{userId}`)
- **DTOs**: Suffixed with purpose (e.g., `UserRegistrationRequest`, `LoginResponse`)

### Data Flow
```
Controller -> Service -> Repository -> Database
         ↓
   Response Wrapper (SuccessResponse/ErrorResponse)
```

### Business Logic
- Financial data uses `DECIMAL(12,2)` for amounts
- Timezone stored per-user (`numina.default-timezone` = America/New_York)
- Currency preferences: 3-char ISO codes (e.g., CAD, USD)
- See `BusinessFunctionalityWalkthrough.md` for detailed use cases

## Integration Points

### Authentication Flow
1. Register: `POST /auth/register` → BCrypt hash → Save user
2. Login: `POST /auth/login` → Validate credentials → Generate JWT (via `JwtUtil`)
3. Protected endpoints: Extract JWT → Validate → Authorize

### Database Connection
- **Local**: `jdbc:mysql://localhost:3306/${MYSQL_DATABASE}`
- **Docker**: Uses service name `db` in connection string
- **HikariCP**: 30s connection timeout, 5s validation

## Common Tasks

### Adding a New Entity
1. Create entity in `entity/` (use Lombok, JPA annotations)
2. Add Flyway migration in `db/migration/`
3. Create repository interface extending `JpaRepository`
4. Implement service layer with business logic
5. Add controller with standardized response wrappers
6. Update OpenAPI config if needed

### Adding a New Endpoint
1. Add method in appropriate controller
2. Use `@Valid` for request body validation
3. Wrap responses in `SuccessResponse` or `ErrorResponse`
4. Update `SecurityConfig` if endpoint needs special auth rules
5. Test via Swagger UI

### Debugging
- **Logs**: Spring Boot colored output enabled (`spring.output.ansi.enabled=ALWAYS`)
- **SQL logging**: `spring.jpa.show-sql=true`
- **Actuator**: Spring Boot Actuator enabled (check `/actuator/health`)
- **Docker logs**: `docker-compose logs -f app`

## Documentation References
- Business requirements: `README.md`
- Database schema: `SchemaDesign.md`
- API examples: `BusinessFunctionalityWalkthrough.md`
- Test rules: `.github/instructions/test-generation.instructions.md`

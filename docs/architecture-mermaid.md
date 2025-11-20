# Numina Backend Architecture (Mermaid Diagrams)

This document visualizes the Numina Personal Finance Management backend using several Mermaid diagrams: high-level component flow, entity relationships, and the authentication sequence.

## 1. Layered Component Architecture
```mermaid
flowchart LR
    subgraph Client
        U[User / Frontend]
    end

    U -->|HTTPS REST| C["Controller Layer<br/>(UserAuthController, future controllers)"]
    C --> S["Service Layer<br/>(UserService, Domain Services)"]
    S --> R["Repository Layer<br/>(UserRepository, JPA Repos)"]
    R --> DB[(MySQL Database)]

    subgraph Config & Infra
        DOT["Dotenv Loader<br/>(.env.*)"] --> CFG[Spring Config]
        FLY[FlywayConfig] --> DB
        OPEN[OpenApiConfig] --> C
    end

    subgraph Security & Auth
        SEC["SecurityConfig<br/>(Spring Security Filter Chain)"] --> C
        JWT["JwtUtil<br/>(JWT Generation/Validation)"] --> SEC
    end

    subgraph Domain Entities
        UE[User Entity]
        AE[Account Entity]
        CE[Category Entity]
        TE[Transaction Entity]
        GE[Goal Entity]
        LE[AuditLog Entity]
    end

    R --> UE
    R --> AE
    R --> CE
    R --> TE
    R --> GE
    R --> LE

    classDef layer fill:#f7f7f7,stroke:#444,stroke-width:1px;
    classDef infra fill:#e3f2fd,stroke:#1e88e5,stroke-width:1px;
    classDef security fill:#ffebee,stroke:#e53935,stroke-width:1px;
    classDef domain fill:#f3e5f5,stroke:#8e24aa,stroke-width:1px;

    C,S,R,DB,U:::layer
    DOT,CFG,FLY,OPEN:::infra
    SEC,JWT:::security
    UE,AE,CE,TE,GE,LE:::domain
```

## 2. Entity Relationship Overview
```mermaid
erDiagram
    USERS ||--o{ ACCOUNTS : owns
    USERS ||--o{ GOALS : sets
    ACCOUNTS ||--o{ TRANSACTIONS : records
    CATEGORIES ||--o{ TRANSACTIONS : classifies
    USERS ||--o{ AUDIT_LOGS : generates

    USERS {
        BIGINT user_id PK
        STRING email
        STRING password_hash
        STRING timezone
        STRING currency
        DATETIME created_at
        DATETIME updated_at
    }
    ACCOUNTS {
        BIGINT account_id PK
        BIGINT user_id FK
        STRING name
        DECIMAL balance
        DATETIME created_at
    }
    CATEGORIES {
        BIGINT category_id PK
        STRING name
        STRING txn_type
    }
    TRANSACTIONS {
        BIGINT transaction_id PK
        BIGINT account_id FK
        BIGINT category_id FK
        DECIMAL amount
        STRING description
        DATE txn_date
        DATETIME created_at
    }
    GOALS {
        BIGINT goal_id PK
        BIGINT user_id FK
        STRING name
        DECIMAL target_amount
        DECIMAL current_amount
        DATE target_date
    }
    AUDIT_LOGS {
        BIGINT audit_id PK
        BIGINT user_id FK
        STRING action
        STRING details
        DATETIME created_at
    }
```

## 3. Authentication (Login) Sequence
```mermaid
sequenceDiagram
    autonumber
    participant User
    participant Controller as UserAuthController
    participant Service as UserService
    participant Repo as UserRepository
    participant JWT as JwtUtil

    User->>Controller: POST /auth/login (email, password)
    Controller->>Service: authenticate(credentials)
    Service->>Repo: findByEmail(email)
    Repo-->>Service: User (with password_hash)
    Service->>Service: verify password (BCrypt)
    Service->>JWT: generateToken(userId, roles)
    JWT-->>Service: jwtString
    Service-->>Controller: LoginResponse(jwtString)
    Controller-->>User: 200 OK + JWT (SuccessResponse)
```

## 4. Request Lifecycle Summary
```mermaid
sequenceDiagram
    participant Client
    participant Controller
    participant Service
    participant Repository
    participant DB as MySQL
    participant JWT as JwtUtil
    participant Sec as SecurityFilterChain

    Client->>Controller: Authenticated Request + JWT
    Controller->>Sec: Delegate security checks
    Sec->>JWT: Validate signature & claims
    JWT-->>Sec: Token valid
    Sec-->>Controller: Proceed
    Controller->>Service: execute business logic
    Service->>Repository: data access
    Repository->>DB: SQL (JPA/Hibernate)
    DB-->>Repository: Resultset
    Repository-->>Service: Entities
    Service-->>Controller: DTO / Response
    Controller-->>Client: SuccessResponse<T>
```

## 5. Possible Extension View (Future Services)
```mermaid
flowchart TB
    subgraph Core Backend
        API[REST API] --> Auth[(JWT Auth)]
        API --> Finance[Finance Services]
        Finance --> Analytics[Analytics Engine]
    end
    Analytics -->|future| ML[(Spending Prediction Model)]
    Finance -->|future| Integrations[(Open Banking / Plaid)]
```

---
### Notes
- Colors distinguish layers (in first diagram) for clarity.
- ER diagram approximates schema using inferred types from instructions.
- Sequence diagrams emphasize stateless JWT validation (no server session persistence).

### Next Ideas
- Add a deployment diagram (Docker Compose + external MySQL + future caching).
- Add performance/monitoring view (Actuator + metrics + logs pipeline).

Feel free to request variations (e.g., dark theme, simplified view, or deployment focus).

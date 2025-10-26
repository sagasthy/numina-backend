# 🗂️ ER Diagram (Conceptual)

Here’s the structure in text form (since I can’t render images directly):

```
 Users ───< Accounts ───< Transactions >─── Categories
   │                          │
   │                          └───< Goals
   │
   └───< AuditLogs
```

- **Users** own **Accounts**.
- **Accounts** contain **Transactions**.
- **Transactions** are linked to **Categories**.
- **Users** can also define **Goals**.
- **AuditLogs** track user/system activity.

---

# 📋 Table Definitions (SQL DDL)

### 1. Users
```sql
CREATE TABLE users (
    user_id          SERIAL PRIMARY KEY,
    email            VARCHAR(255) UNIQUE NOT NULL,
    password_hash    VARCHAR(255) NOT NULL,
    full_name        VARCHAR(100),
    currency_pref    CHAR(3) DEFAULT 'USD',
    timezone         VARCHAR(50) DEFAULT 'UTC',
    two_factor_enabled BOOLEAN DEFAULT FALSE,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

### 2. Accounts
```sql
CREATE TABLE accounts (
    account_id       SERIAL PRIMARY KEY,
    user_id          INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    account_name     VARCHAR(100) NOT NULL,
    account_type     VARCHAR(50) CHECK (account_type IN ('checking','savings','credit','investment','cash')),
    currency         CHAR(3) DEFAULT 'USD',
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

### 3. Categories
```sql
CREATE TABLE categories (
    category_id      SERIAL PRIMARY KEY,
    user_id          INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    category_name    VARCHAR(100) NOT NULL,
    category_type    VARCHAR(20) CHECK (category_type IN ('income','expense')),
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

### 4. Transactions
```sql
CREATE TABLE transactions (
    transaction_id   SERIAL PRIMARY KEY,
    account_id       INT NOT NULL REFERENCES accounts(account_id) ON DELETE CASCADE,
    category_id      INT REFERENCES categories(category_id),
    amount           DECIMAL(12,2) NOT NULL,
    transaction_type VARCHAR(20) CHECK (transaction_type IN ('income','expense','transfer')),
    description      TEXT,
    transaction_date DATE NOT NULL,
    is_recurring     BOOLEAN DEFAULT FALSE,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

### 5. Goals
```sql
CREATE TABLE goals (
    goal_id          SERIAL PRIMARY KEY,
    user_id          INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    goal_name        VARCHAR(100) NOT NULL,
    target_amount    DECIMAL(12,2) NOT NULL,
    current_amount   DECIMAL(12,2) DEFAULT 0,
    deadline         DATE,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

### 6. Audit Logs
```sql
CREATE TABLE audit_logs (
    log_id           SERIAL PRIMARY KEY,
    user_id          INT REFERENCES users(user_id) ON DELETE SET NULL,
    action           VARCHAR(255) NOT NULL,
    ip_address       INET,
    user_agent       VARCHAR(255),
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

# 🔑 Notes on Design
- **Referential integrity**: Foreign keys ensure data consistency (e.g., deleting a user cascades to their accounts/transactions).
- **Audit logs**: Useful for compliance simulation and debugging.
- **Categories**: User-specific, so each user can customize their own expense/income categories.
- **Goals**: Tied to users, not accounts, so they can span multiple accounts.
- **Transactions**: Flexible enough to support recurring entries and transfers.

---

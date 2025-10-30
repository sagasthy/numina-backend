CREATE TABLE IF NOT EXISTS users (
    user_id          INT PRIMARY KEY AUTO_INCREMENT,
    email            VARCHAR(255) UNIQUE NOT NULL,
    password_hash    VARCHAR(255) NOT NULL,
    full_name        VARCHAR(100),
    currency_pref    CHAR(3) DEFAULT 'USD',
    timezone         VARCHAR(50) DEFAULT 'UTC',
    two_factor_enabled BOOLEAN DEFAULT FALSE,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS accounts (
    account_id       INT PRIMARY KEY AUTO_INCREMENT,
    user_id          INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    account_name     VARCHAR(100) NOT NULL,
    account_type     VARCHAR(50) CHECK (account_type IN ('checking','savings','credit','investment','cash')),
    currency         CHAR(3) DEFAULT 'USD',
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS categories (
    category_id      INT PRIMARY KEY AUTO_INCREMENT,
    user_id          INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    category_name    VARCHAR(100) NOT NULL,
    category_type    VARCHAR(20) CHECK (category_type IN ('income','expense')),
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS transactions (
    transaction_id   INT PRIMARY KEY AUTO_INCREMENT,
    account_id       INT NOT NULL REFERENCES accounts(account_id) ON DELETE CASCADE,
    category_id      INT REFERENCES categories(category_id),
    amount           DECIMAL(12,2) NOT NULL,
    transaction_type VARCHAR(20) CHECK (transaction_type IN ('income','expense','transfer')),
    description      TEXT,
    transaction_date DATE NOT NULL,
    is_recurring     BOOLEAN DEFAULT FALSE,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS goals (
    goal_id          INT PRIMARY KEY AUTO_INCREMENT,
    user_id          INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    goal_name        VARCHAR(100) NOT NULL,
    target_amount    DECIMAL(12,2) NOT NULL,
    current_amount   DECIMAL(12,2) DEFAULT 0,
    deadline         DATE,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS audit_logs (
    log_id           INT PRIMARY KEY AUTO_INCREMENT,
    user_id          INT REFERENCES users(user_id) ON DELETE SET NULL,
    action           VARCHAR(255) NOT NULL,
    ip_address       VARBINARY(16),
    user_agent       VARCHAR(255),
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

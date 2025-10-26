# 📊 Business Requirements for a Financial Backend Web Application called "Numina" 
Numina -> from "numinous" and "numbers".

## 1. **Project Overview**
The system will be a **Personal Finance Management (PFM) Platform** that allows users to:
- Create accounts and securely log in
- Track income, expenses, and savings
- Categorize transactions
- Generate reports and insights
- Set financial goals and monitor progress

This is realistic enough to mimic real-world financial systems, but scoped so you can build it in your free time.

---

## 2. **Core Functional Requirements**

### 🔐 User Management
- **User Registration & Authentication**
    - Register with email, password, and optional 2FA.
    - Login/logout with JWT-based session management.
    - Password reset via email.
- **User Profile**
    - Update personal details (name, currency preference, time zone).
    - Manage security settings (2FA, password change).

### 💰 Account & Wallet Management
- Users can create multiple **accounts** (e.g., Checking, Savings, Credit Card).
- Each account has:
    - Account name
    - Account type (bank, cash, credit, investment)
    - Balance (auto-calculated from transactions)
    - Currency (support multi-currency)

### 🧾 Transactions
- Add, edit, delete transactions.
- Transaction fields:
    - Date
    - Amount
    - Type (income/expense/transfer)
    - Category (Food, Rent, Salary, etc.)
    - Notes
    - Linked account
- Support **recurring transactions** (e.g., monthly rent).

### 📈 Reporting & Analytics
- Generate reports:
    - Monthly income vs. expenses
    - Spending by category
    - Net worth over time
- Export reports to CSV/JSON.
- Provide simple **visualization-ready APIs** (so you can later plug in charts).

### 🎯 Financial Goals
- Users can set goals (e.g., save $5,000 in 6 months).
- Track progress automatically based on account balances.

### 🔎 Audit & History
- Maintain transaction history with timestamps.
- Provide an API for retrieving logs (for debugging and compliance simulation).

---

## 3. **Non-Functional Requirements**

- **Security**
    - Encrypt sensitive data (passwords with bcrypt/argon2).
    - Use HTTPS/TLS.
    - Role-based access control (basic: user vs. admin).
- **Performance**
    - Handle at least 100 concurrent users (simulate load).
    - API response time < 300ms for standard queries.
- **Scalability**
    - Modular architecture (separate auth, transactions, reporting).
    - Ready for containerization (Docker).
- **Reliability**
    - Database transactions must ensure ACID compliance.
    - Backup and restore mechanism for data.
- **Compliance Simulation**
    - Store audit logs (mimic financial compliance).
    - GDPR-style "delete my data" endpoint.

---

## 4. **Technical Requirements**

- **Backend Framework**: Node.js (Express, NestJS) or Python (FastAPI, Django REST).
- **Database**: PostgreSQL (relational, strong for financial data).
- **Authentication**: JWT + Refresh Tokens.
- **API Design**: REST (or GraphQL if you want a challenge).
- **Testing**: Unit + Integration tests (Jest, Pytest).
- **Deployment**: Docker + CI/CD pipeline (GitHub Actions).

---

## 5. **Sample API Endpoints**

| Endpoint                  | Method | Description |
|----------------------------|--------|-------------|
| `/auth/register`           | POST   | Register new user |
| `/auth/login`              | POST   | Authenticate user |
| `/users/me`                | GET    | Get profile info |
| `/accounts`                | POST   | Create new account |
| `/accounts/:id`            | GET    | Get account details |
| `/transactions`            | POST   | Add transaction |
| `/transactions/:id`        | PUT    | Update transaction |
| `/reports/monthly`         | GET    | Get monthly report |
| `/goals`                   | POST   | Create financial goal |
| `/audit/logs`              | GET    | Retrieve activity logs |

---

## 6. **Stretch Features (Optional)**
- **Bank API Integration** (Plaid, Yodlee) for real-time sync.
- **Budget Alerts**: Notify when spending exceeds a threshold.
- **Investment Tracking**: Track stock/crypto portfolios.
- **AI Insights**: Categorize transactions automatically.

---

✅ With this scope, you’ll get to practice:
- **Database schema design** (accounts, transactions, users, goals).
- **API development** (CRUD, authentication, reporting).
- **Security best practices** (encryption, audit logs).
- **Scalable architecture** (modular services, containerization).

---

# 🏦 Business Functionality with Examples

## 1. **User Management**
**Functionality:** Securely register, log in, and manage profile.  
**Example:**
- Siva signs up with his email and password.
- He enables **2FA** for extra security.
- Later, he updates his profile to set his preferred currency to **CAD** and timezone to **EST**.

👉 Business value: Ensures secure access and personalized experience.

### Register
**Request**
```http
POST /auth/register
Content-Type: application/json
```
```json
{
  "email": "siva@example.com",
  "password": "StrongPass123!",
  "full_name": "Siva Kumar",
  "currency_pref": "CAD",
  "timezone": "America/Toronto"
}
```

**Response**
```json
{
  "user_id": 1,
  "email": "siva@example.com",
  "currency_pref": "CAD",
  "timezone": "America/Toronto",
  "created_at": "2025-10-26T21:20:00Z"
}
```

---

## 2. **Account & Wallet Management**
**Functionality:** Users can create multiple accounts to represent real-world financial sources.  
**Example:**
- Siva creates three accounts:
    - *Checking Account* (CAD)
    - *Savings Account* (CAD)
    - *Credit Card* (CAD)
- Each account maintains its own balance, updated automatically when transactions are added.

👉 Business value: Mirrors real-world financial structure, enabling accurate tracking.

### Create Account
**Request**
```http
POST /accounts
```
```json
{
  "account_name": "Checking Account",
  "account_type": "checking",
  "currency": "CAD"
}
```

**Response**
```json
{
  "account_id": 101,
  "account_name": "Checking Account",
  "account_type": "checking",
  "currency": "CAD",
  "balance": 0.00
}
```
---

## 3. **Transactions**
**Functionality:** Record income, expenses, and transfers.  
**Example:**
- Income: Siva adds a transaction of **+3000 CAD** on the *Checking Account* with category *Salary*.
- Expense: He logs **-1200 CAD** for *Rent* in the *Checking Account*.
- Transfer: He moves **500 CAD** from *Checking* to *Savings*.

👉 Business value: Provides a detailed financial history and real-time balance updates.

### Add Income
**Request**
```http
POST /transactions
```
```json
{
  "account_id": 101,
  "category_id": 1,
  "amount": 3000.00,
  "transaction_type": "income",
  "description": "October Salary",
  "transaction_date": "2025-10-01"
}
```

**Response**
```json
{
  "transaction_id": 5001,
  "account_id": 101,
  "amount": 3000.00,
  "transaction_type": "income",
  "balance_after": 3000.00
}
```

---

### Add Expense
**Request**
```http
POST /transactions
```
```json
{
  "account_id": 101,
  "category_id": 2,
  "amount": 1200.00,
  "transaction_type": "expense",
  "description": "October Rent",
  "transaction_date": "2025-10-02"
}
```

**Response**
```json
{
  "transaction_id": 5002,
  "account_id": 101,
  "amount": -1200.00,
  "transaction_type": "expense",
  "balance_after": 1800.00
}
```
---

## 4. **Categories**
**Functionality:** Organize transactions into meaningful groups.  
**Example:**
- Siva creates categories: *Groceries, Rent, Utilities, Entertainment, Salary*.
- When he logs a **-200 CAD** grocery purchase, it’s categorized under *Groceries*.

👉 Business value: Enables spending analysis and budgeting insights.

---

## 5. **Reporting & Analytics**
**Functionality:** Generate insights from transaction data.  
**Example:**
- Monthly Report: In October, Siva’s income = **3000 CAD**, expenses = **2200 CAD**, net savings = **800 CAD**.
- Category Breakdown: *Rent* = 55% of expenses, *Groceries* = 20%, *Entertainment* = 10%.

👉 Business value: Helps users understand spending habits and make better financial decisions.

### Monthly Report
**Request**
```http
GET /reports/monthly?month=2025-10
```

**Response**
```json
{
  "month": "October 2025",
  "income": 3000.00,
  "expenses": 2200.00,
  "net_savings": 800.00,
  "by_category": {
    "Rent": 1200.00,
    "Groceries": 400.00,
    "Entertainment": 200.00,
    "Utilities": 400.00
  }
}
```
---

## 6. **Financial Goals**
**Functionality:** Set and track savings goals.  
**Example:**
- Siva sets a goal: *Save 5000 CAD for a vacation by June 2026*.
- Each time he transfers money into *Savings Account*, the system updates his progress.
- After 3 months, he has saved **1500 CAD**, so the system shows **30% progress**.

👉 Business value: Motivates users to save and track progress toward financial milestones.

### Create Goal
**Request**
```http
POST /goals
```
```json
{
  "goal_name": "Vacation Savings",
  "target_amount": 5000.00,
  "deadline": "2026-06-01"
}
```

**Response**
```json
{
  "goal_id": 301,
  "goal_name": "Vacation Savings",
  "target_amount": 5000.00,
  "current_amount": 0.00,
  "deadline": "2026-06-01",
  "progress": "0%"
}
```
---

## 7. **Audit & History**
**Functionality:** Track all user actions for compliance and debugging.  
**Example:**
- When Siva logs in, adds a transaction, or deletes a goal, an entry is created in the **audit_logs** table.
- If there’s a dispute (e.g., “I didn’t delete that transaction”), the admin can review the logs.

👉 Business value: Provides accountability, transparency, and compliance simulation.

### Example Log Entry
**Response from `/audit/logs`**
```json
[
  {
    "log_id": 9001,
    "user_id": 1,
    "action": "Created transaction 5002 (expense)",
    "ip_address": "192.168.1.10",
    "user_agent": "Mozilla/5.0",
    "created_at": "2025-10-02T14:05:00Z"
  }
]
```

---

# 🔑 How This All Ties Together
- **User registers → creates accounts → logs transactions → categorizes them → views reports → sets goals → system tracks everything in audit logs.**
- This flow mimics real-world financial apps like Mint, YNAB, or banking dashboards, but scoped for your project.

---

# Numina Backend - Test Data Generator Prompt

## Purpose
Generate realistic test data to validate code fixes or enhancements when unit tests are not available. The scope of test data generation is determined by analyzing uncommitted changes in the Git repository.

## Instructions

1. **Detect Change Scope**
  - Use `git diff` to identify files and lines modified but not yet committed.
  - Focus on changes in the `controller/`, `service/`, `entity/`, and `model/` packages.
  - For database-related changes, include relevant schema migrations from `db/migration/`.

2. **Analyze Impact**
  - Determine which entities, DTOs, or business logic are affected.
  - Identify input/output data structures and any new/modified fields.

3. **Generate Test Data**
  - For each affected entity or DTO, create sample JSON objects or SQL insert statements.
  - Ensure data covers edge cases, typical values, and boundary conditions relevant to the change.
  - For financial data, use realistic amounts (DECIMAL(12,2)), valid currency codes, and timezone-aware timestamps.

4. **Format**
  - Output test data in a format suitable for manual API testing (e.g., Swagger UI), database seeding, or direct service invocation.
  - Clearly label each test data set with the affected endpoint, entity, or business logic.

5. **Example Output**
  ```
  ## Test Data for PATCH /accounts/{accountId}

  Request Body:
  {
    "accountName": "Emergency Fund",
    "currency": "USD",
    "balance": 2500.00
  }

  ## SQL Insert for transactions table
  INSERT INTO transactions (user_id, account_id, category_id, amount, currency, transaction_date)
  VALUES (1, 2, 3, 150.75, 'USD', '2024-06-01T10:00:00-04:00');
  ```

6. **Validation**
  - Ensure generated data matches the schema and validation rules defined in the codebase.
  - Include both valid and intentionally invalid examples if input validation is part of the change.

## Notes
- Do not generate test code or step definitions.
- Only produce data relevant to the detected scope of change.
- Reference the latest code and migration files for field names and constraints.
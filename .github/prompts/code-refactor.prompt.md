**Code Refactor Prompt**

Objective: Refactor the provided code to improve readability, testability, and maintainability while strictly preserving existing functionality and public contracts.

Guiding Principles:
1. Functionality Preservation: Do not change observable behavior, business rules, data persistence semantics, security constraints, auth flow, or API response schemas (e.g., `SuccessResponse`, `ErrorResponse`).
2. Readability: Improve naming, logical structure, and reduce cognitive load. Prefer early returns over deep nesting. Remove dead code and redundant comments.
3. Maintainability: Eliminate duplication, extract cohesive helpers/services, enforce clear separation of concerns (Controller -> Service -> Repository). Keep classes single-purpose.
4. Testability: Favor dependency injection, avoid hidden side effects, isolate external integrations, and make logic deterministic where feasible. Do not introduce static global state.
5. Backward Compatibility: Keep method signatures, DTO field names, JSON structure, and security configurations stable unless explicitly approved.

Required Actions:
- Simplify overly complex methods (split into smaller private helpers with clear intent).
- Replace implicit flows with explicit, self-documenting logic (e.g., descriptive variable and method names).
- Consolidate duplicated logic (e.g., repeated validation or mapping code) into shared components.
- Ensure services do not perform controller-specific tasks (like building HTTP responses).
- Strengthen null-safety and validation using existing Spring Validation / annotations where appropriate.
- Retain existing logging level and semantics; add logging only if it aids debuggability of complex flows.

Testing & Verification:
- Run full test suite: `./mvnw clean test` — all existing tests must pass unchanged.
- Add new unit tests ONLY for previously untested edge cases or critical paths; do NOT auto-generate Cucumber step definitions (per project instructions).
- If a new test is added, justify it with a brief note (e.g., uncovered branch, prevented regression).
- Avoid broad integration test additions unless essential for verifying preserved behavior.

Prohibited Changes:
- Altering authentication logic or JWT generation/validation.
- Changing database schema or Flyway migrations (unless separately approved).
- Introducing new external dependencies without necessity.
- Modifying response wrapper structure or HTTP status conventions.

Refactor Checklist Before Submission:
1. Code compiles and all tests pass.
2. Public API behavior (endpoints, JSON) unchanged.
3. No removed security rules or relaxed access controls.
4. No new warnings from the build (unless fixing existing ones).
5. Complexity reduced in targeted areas (e.g., cyclomatic complexity, duplicated blocks).

Deliverables:
- Summary of each major refactor (file/class, what changed, why, and how functionality was preserved).
- List of any new tests and their coverage purpose.
- Confirmation statement: "All existing functionality retained; all tests passing." (Run after verification.)

Focus strictly on improvements that advance readability, testability, and maintainability without feature drift.


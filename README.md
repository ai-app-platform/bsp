# Deposit Services BSP

Spring Boot **3.5.8**, Java **21**, Maven, MapStruct and Oracle Database.

## Features

- Account CRUD REST API.
- Money transfer create/read/update/delete endpoints.
- Atomic debit/credit in a database transaction.
- Pessimistic account row locks to reduce concurrent balance races.
- `BigDecimal` monetary values, Bean Validation and centralized API errors.
- MapStruct DTO/entity mapping.
- Oracle connection configuration via environment variables.

> **Important:** This is a starter project, not a certified banking core. Completed transfers are immutable and cannot be updated or deleted, because changing historical transfer records without a compensating ledger entry would corrupt the audit trail. For production, add authentication/authorization, idempotency keys, audit logging, limits, reconciliation, migration-managed schema, observability, and security review.

## Requirements

- JDK 21
- Maven 3.9+
- Oracle Database reachable by the application

## Configure Oracle

Set environment variables (or edit `src/main/resources/application.yml`):

```bash
export ORACLE_DB_URL='jdbc:oracle:thin:@//localhost:1521/FREEPDB1'
export ORACLE_DB_USERNAME='BSP_APP'
export ORACLE_DB_PASSWORD='change-me'
```

The configured account/schema must have permission to create/alter tables if using `ddl-auto: update`.
For production, use Flyway/Liquibase migrations and set `spring.jpa.hibernate.ddl-auto=validate`.

## Run

```bash
mvn clean spring-boot:run
```

Health endpoint: `GET http://localhost:8080/actuator/health`

## API

### Accounts

- `POST /api/v1/accounts`
- `GET /api/v1/accounts`
- `GET /api/v1/accounts/{id}`
- `PUT /api/v1/accounts/{id}`
- `DELETE /api/v1/accounts/{id}` (blocked if account has transfer history)

Example create account:

```json
{
  "accountNumber": "ACC-10001",
  "holderName": "Alex Morgan",
  "currency": "USD",
  "initialBalance": 1000.00
}
```

### Transfers

- `POST /api/v1/transfers` — executes the transfer immediately and stores a completed transfer.
- `GET /api/v1/transfers`
- `GET /api/v1/transfers/{id}`
- `PUT /api/v1/transfers/{id}` — completed transfers are immutable; returns conflict.
- `DELETE /api/v1/transfers/{id}` — completed transfers are immutable; returns conflict.

Example transfer:

```json
{
  "sourceAccountId": 1,
  "destinationAccountId": 2,
  "amount": 125.50,
  "description": "Invoice payment"
}
```

The source and destination accounts must use the same currency. Transfers to the same account are rejected. The source account must have sufficient funds.

## Sample curl

```bash
curl -X POST http://localhost:8080/api/v1/accounts \
  -H 'Content-Type: application/json' \
  -d '{"accountNumber":"ACC-10001","holderName":"Alex Morgan","currency":"USD","initialBalance":1000.00}'

curl -X POST http://localhost:8080/api/v1/accounts \
  -H 'Content-Type: application/json' \
  -d '{"accountNumber":"ACC-10002","holderName":"Jamie Lee","currency":"USD","initialBalance":100.00}'

curl -X POST http://localhost:8080/api/v1/transfers \
  -H 'Content-Type: application/json' \
  -d '{"sourceAccountId":1,"destinationAccountId":2,"amount":125.50,"description":"Invoice payment"}'
```

IDs in the examples are illustrative; use IDs returned by the API.

## Package layout

```text
ir.dotin.depositservices.bsp
├── BspApplication.java
├── config/
├── controller/
├── dto/
├── entity/
├── exception/
├── mapper/
├── repository/
└── service/
```

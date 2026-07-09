# E-Commerce Backend - Week 6: JPA & Liquibase

REST API for e-commerce with Spring Boot 3, Spring Data JPA, MySQL, and Liquibase database migrations.

## Tech Stack

- **Java 17+** — Language
- **Spring Boot 3.3.2** — Framework
- **Spring Data JPA** — ORM layer
- **Hibernate** — JPA implementation
- **Liquibase** — Database version control
- **MySQL 8** — Production database
- **H2** — In-memory database for testing

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8 (via XAMPP or standalone)
- Postman (for API testing)

## Project Structure

```
src/main/java/com/example/demo/
├── controller/              REST endpoints
├── service/                 Business logic
├── repository/              Data access (JPA)
├── model/                   Entity definitions
└── exception/              (Future: custom exceptions)

src/main/resources/
├── application.properties   Configuration
└── db/changelog/           Liquibase migrations
    ├── db.changelog-master.xml
    └── versions/v1.0/
        └── 001-create-users-table.xml
```

## Setup Instructions

### 1. Database Setup

Start MySQL via XAMPP:
```bash
# Open XAMPP Control Panel
# Click Start next to MySQL
# Verify "Running" status appears in green
```

Create the database:
```bash
# Option A: Via phpMyAdmin (http://localhost/phpmyadmin/)
# Click New → Database name: "ecommerce_db" → Create

# Option B: Via MySQL CLI
mysql -u root -p
CREATE DATABASE ecommerce_db;
```

### 2. Run Application

```bash
cd c:\Users\KaranMaheshwari\Desktop\Assignment\E-Commerce

# Build
.\mvnw.cmd clean package

# Run
.\mvnw.cmd spring-boot:run
```

The app starts on `http://localhost:8080`

**Console output should show:**
```
Liquibase: Successfully acquired change log lock
Liquibase: Creating database schema history table...
Liquibase: Changelog has been marked as executed...
Started DemoApplication in X.XXXs
```

### 3. Run Tests

```bash
.\mvnw.cmd test
```

Tests use an in-memory H2 database configured in `src/test/resources/application.properties`

## API Endpoints

### Create User
```http
POST /users
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com"
}

Response: 201 Created
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "createdAt": "2026-07-09T10:30:45",
  "updatedAt": "2026-07-09T10:30:45"
}
```

### Get All Users
```http
GET /users

Response: 200 OK
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "createdAt": "2026-07-09T10:30:45",
    "updatedAt": "2026-07-09T10:30:45"
  }
]
```

### Get User by ID
```http
GET /users/1

Response: 200 OK
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "createdAt": "2026-07-09T10:30:45",
  "updatedAt": "2026-07-09T10:30:45"
}
```

### Update User
```http
PUT /users/1
Content-Type: application/json

{
  "name": "Jane Doe",
  "email": "jane@example.com"
}

Response: 200 OK
{
  "id": 1,
  "name": "Jane Doe",
  "email": "jane@example.com",
  "createdAt": "2026-07-09T10:30:45",
  "updatedAt": "2026-07-09T10:35:12"
}
```

### Delete User
```http
DELETE /users/1

Response: 204 No Content
```

## Database Migrations (Liquibase)

Migrations are stored in `src/main/resources/db/changelog/`

### Current Migrations

**v1.0/001-create-users-table.xml**
- Creates `users` table with auto-increment ID
- Adds columns: name, email, created_at, updated_at
- Creates unique index on email
- Includes rollback logic

### Add New Migration

1. Create new file: `src/main/resources/db/changelog/versions/v1.0/002-your-change.xml`
2. Add changeSet with your modifications
3. Include file in `db.changelog-master.xml`
4. Run app — Liquibase auto-applies changes

## Validation & Error Handling

**Validation Rules:**
- `name` — Required, cannot be blank
- `email` — Required, cannot be blank, must be unique

**HTTP Status Codes:**
- `200 OK` — Success
- `201 Created` — Resource created
- `204 No Content` — Resource deleted
- `400 Bad Request` — Validation error
- `500 Internal Server Error` — Server error

## Postman Collection

Import `postman/E-Commerce-Week6.postman_collection.json` into Postman for pre-configured API requests.

## Testing

### Unit Tests
```bash
.\mvnw.cmd test
```

Uses H2 in-memory database — no external DB required for tests.

### Manual Testing
Use Postman or curl to test endpoints against running application.

## Best Practices Used

✅ **Layered Architecture** — Controller → Service → Repository
✅ **JPA & Hibernate** — Type-safe ORM with automatic SQL generation
✅ **Liquibase** — Version-controlled database schema
✅ **Audit Fields** — `createdAt` and `updatedAt` timestamps
✅ **Constructor Injection** — Dependency injection best practice
✅ **Validation** — Input validation at service layer
✅ **Proper HTTP Status Codes** — Semantic HTTP responses
✅ **Clean Code** — Minimal comments, expressive naming

## Next Steps (Week 7)

- JWT authentication
- Spring Security integration
- Login/Register endpoints
- Role-based access control (ADMIN/USER)
- Protected endpoints

## Troubleshooting

| Issue | Solution |
|---|---|
| MySQL not starting | Run XAMPP as Administrator |
| Port 3306 in use | Check if another MySQL instance is running |
| Database not created | Verify in phpMyAdmin; create manually if needed |
| Liquibase errors | Check `databasechangelog` table in phpMyAdmin |
| App won't start | Verify MySQL is running; check application.properties |
| Tests fail | H2 database in-memory; no external setup needed |

## References

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/)
- [Liquibase Documentation](https://docs.liquibase.com/)
- [Hibernate Guide](https://hibernate.org/)

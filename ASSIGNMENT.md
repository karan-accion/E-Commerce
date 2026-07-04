# Week 1 Assignment — User API with JSON Storage

This is a minimalist Spring Boot REST API that manages users using a JSON file as storage. It implements the basic CRUD pattern with proper layering.

## Project Structure

```
src/main/java/com/example/demo/
├── controller/          REST endpoints
├── service/             Business logic
├── repository/          Data access (JSON file backed)
├── model/               Entity definitions
└── config/              Configuration properties
```

## Quick Start

1. Build the project:
   ```sh
   .\mvnw.cmd clean package
   ```

2. Run the app:
   ```sh
   .\mvnw.cmd spring-boot:run
   ```

3. The app will start on `http://localhost:8080`

## API Endpoints

### Get all users
```http
GET /users
```

### Get a user by ID
```http
GET /users/{id}
```

### Create a new user
```http
POST /users
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com"
}
```
Returns `201 Created` with the created user (now has an auto-generated ID).

### Validation

- `name` and `email` are required fields
- If missing, the API returns `400 Bad Request`

## Storage

User data is persisted to `./data/users.json`. The file is created automatically on first write.

## Testing

Run the test suite:
```sh
.\mvnw.cmd test
```

The test class is at `src/test/java/com/example/demo/controller/UserControllerTest.java`.

# Week 6 — Testing Guide

## Quick Links

- **phpMyAdmin**: http://localhost/phpmyadmin/
- **Spring Boot App**: http://localhost:8080
- **Postman Collection**: `postman/E-Commerce-Week6.postman_collection.json`
- **SQL Setup Script**: `sql/setup-database.sql`

---

## Testing Workflow

### 1. Start MySQL in XAMPP
- Open XAMPP Control Panel
- Click **Start** next to MySQL (should turn green)

### 2. Create Database
- Open: http://localhost/phpmyadmin/
- Click **New** → Type `ecommerce_db` → Click **Create**
- (Alternatively, run the SQL script in phpMyAdmin SQL tab)

### 3. Start Spring Boot App
```powershell
cd 'c:\Users\KaranMaheshwari\Desktop\Assignment\E-Commerce'
.\mvnw.cmd spring-boot:run
```

### 4. Run Postman Tests
1. Open Postman
2. File → Import → Select `E-Commerce-Week6.postman_collection.json`
3. Run requests in order (see below)

---

## Postman Test Sequence

### Test 1: Create a User (POST)
```
POST http://localhost:8080/users

Body (JSON):
{
  "name": "John Doe",
  "email": "john@example.com"
}

Expected Response (201 Created):
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com"
}
```

### Test 2: Create Another User
```
POST http://localhost:8080/users

Body (JSON):
{
  "name": "Jane Smith",
  "email": "jane@example.com"
}

Expected Response (201 Created):
{
  "id": 2,
  "name": "Jane Smith",
  "email": "jane@example.com"
}
```

### Test 3: Get All Users (GET)
```
GET http://localhost:8080/users

Expected Response (200 OK):
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com"
  },
  {
    "id": 2,
    "name": "Jane Smith",
    "email": "jane@example.com"
  }
]
```

### Test 4: Get Specific User (GET)
```
GET http://localhost:8080/users/1

Expected Response (200 OK):
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com"
}
```

### Test 5: Update User (PUT)
```
PUT http://localhost:8080/users/1

Body (JSON):
{
  "name": "John Updated",
  "email": "john.updated@example.com"
}

Expected Response (200 OK):
{
  "id": 1,
  "name": "John Updated",
  "email": "john.updated@example.com"
}
```

### Test 6: Delete User (DELETE)
```
DELETE http://localhost:8080/users/2

Expected Response (204 No Content)
```

### Test 7: Verify Deletion (GET)
```
GET http://localhost:8080/users

Expected Response (200 OK):
[
  {
    "id": 1,
    "name": "John Updated",
    "email": "john.updated@example.com"
  }
]
```

---

## Verify Data in Database

### Via phpMyAdmin
1. Open: http://localhost/phpmyadmin/
2. Click **ecommerce_db** → **users** table
3. You should see all the users you created

### Via Spring Boot Console
When running the app, you should see SQL queries logged:
```
Hibernate: insert into users (email, name) values (?, ?)
Hibernate: select u1_0.id, u1_0.email, u1_0.name from users u1_0
Hibernate: update users set email=?, name=? where id=?
Hibernate: delete from users where id=?
```

---

## Error Handling

### Validation Errors
If you send a request with missing `name` or `email`:
```
Status: 400 Bad Request
Message: "Name is required" or "Email is required"
```

### Not Found
If you request a user ID that doesn't exist:
```
Status: 500 Internal Server Error
Message: "User not found with id: 999"
```

### Duplicate Email
If you try to create a user with an email that already exists:
```
Status: 500 Internal Server Error
Message: Duplicate entry for email
```

---

## Troubleshooting

| Issue | Solution |
|---|---|
| MySQL won't start | Run XAMPP as Administrator |
| Can't connect to database | Verify `ecommerce_db` exists in phpMyAdmin |
| Spring Boot won't start | Check if port 8080 is available; check Java is installed |
| Postman shows "Cannot GET /users" | Verify Spring Boot app is running (should see "Started DemoApplication..." in console) |
| Data doesn't persist | Check that `spring.jpa.hibernate.ddl-auto=update` is in application.properties |


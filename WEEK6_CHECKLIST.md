# Week 6 Setup Checklist

## Before You Start
- [ ] Java 17+ installed and `java -version` works
- [ ] XAMPP installed
- [ ] Postman installed

---

## Step 1: Database Setup (5 minutes)

- [ ] Open **XAMPP Control Panel**
- [ ] Click **Start** next to MySQL
- [ ] Wait for status to show **Running** in green
- [ ] Open browser: http://localhost/phpmyadmin/
- [ ] Create new database named `ecommerce_db`
- [ ] (Optional) Run SQL script: `sql/setup-database.sql` to add sample data

---

## Step 2: Postman Setup (2 minutes)

- [ ] Open Postman
- [ ] Click **File** → **Import**
- [ ] Select file: `postman/E-Commerce-Week6.postman_collection.json`
- [ ] Verify collection **E-Commerce Week 6** appears in sidebar

---

## Step 3: Run Spring Boot App (3 minutes)

- [ ] Open PowerShell
- [ ] Navigate: `cd 'c:\Users\KaranMaheshwari\Desktop\Assignment\E-Commerce'`
- [ ] Run: `.\mvnw.cmd spring-boot:run`
- [ ] Wait for message: **"Started DemoApplication in X.XXXs"**
- [ ] Verify no errors in console

---

## Step 4: Test APIs with Postman (5 minutes)

- [ ] In Postman, expand **E-Commerce Week 6** → **User CRUD**
- [ ] Click **POST - Create User** → **Send**
  - Should return status **201 Created**
- [ ] Click **GET - All Users** → **Send**
  - Should return status **200 OK** with array of users
- [ ] Click **GET - User by ID** → **Send**
  - Change ID in URL to match created user
- [ ] Click **PUT - Update User** → **Send**
  - Should return status **200 OK** with updated data
- [ ] Click **DELETE - Remove User** → **Send**
  - Should return status **204 No Content**

---

## Step 5: Verify Data in Database (2 minutes)

- [ ] Open browser: http://localhost/phpmyadmin/
- [ ] Click **ecommerce_db** → **users** table
- [ ] Verify users are persisted from your Postman tests

---

## Files Created for Week 6

```
project-root/
├── WEEK6_SETUP.md                    ← Detailed setup guide
├── TESTING_GUIDE.md                  ← Testing instructions & examples
├── postman/
│   └── E-Commerce-Week6.postman_collection.json  ← Import into Postman
├── sql/
│   └── setup-database.sql            ← SQL script to create DB & tables
└── src/
    ├── main/
    │   ├── resources/
    │   │   └── application.properties ← Updated for MySQL (✅ done)
    │   └── java/com/example/demo/
    │       ├── model/User.java        ← Updated to JPA Entity (✅ done)
    │       ├── repository/UserRepository.java  ← Now extends JpaRepository (✅ done)
    │       ├── service/UserService.java        ← Updated for Long IDs (✅ done)
    │       └── controller/UserController.java  ← Added PUT/DELETE (✅ done)
    └── test/
        ├── resources/
        │   └── application.properties ← H2 in-memory DB for tests (✅ created)
        └── java/.../UserControllerTest.java    ← Updated (✅ done)
```

---

## Common Commands

Start MySQL:
```
XAMPP Control Panel → MySQL → Start
```

Run the app:
```powershell
.\mvnw.cmd spring-boot:run
```

Run tests:
```powershell
.\mvnw.cmd test
```

View database:
```
http://localhost/phpmyadmin/
```

---

## Expected Outcomes

After completing all steps:
- ✅ MySQL database `ecommerce_db` exists with `users` table
- ✅ Spring Boot app runs without errors on port 8080
- ✅ All 5 CRUD endpoints work via Postman
- ✅ User data persists in MySQL (survives app restart)
- ✅ Log output shows Hibernate SQL queries

---

## What Changed from Week 5?

| Aspect | Week 5 | Week 6 |
|---|---|---|
| Storage | JSON file (`users.json`) | MySQL database |
| ID Type | UUID | Long (auto-increment) |
| Repository | Custom file I/O | JpaRepository |
| Persistence | Manual JSON serialization | Hibernate ORM |
| Endpoints | GET, POST only | GET, POST, PUT, DELETE |
| Database Tools | None | XAMPP + phpMyAdmin |

---

## Next Steps (Week 7)

- Add authentication with JWT
- Create Login/Register endpoints
- Secure endpoints with Spring Security
- Add Role-based access control

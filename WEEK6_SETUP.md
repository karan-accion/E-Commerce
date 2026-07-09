# Week 6 Assignment — Spring Data JPA with MySQL & XAMPP

This guide walks you through setting up XAMPP for MySQL database, configuring Postman for testing, and migrating from JSON storage to a real database using Spring Data JPA and Hibernate.

---

## Part 1: Install & Setup XAMPP

### Step 1 — Download XAMPP
1. Go to: https://www.apachefriends.org/
2. Download **XAMPP for Windows** (latest version)
3. Run the installer and follow the prompts
4. Install to default location: `C:\xampp`

### Step 2 — Start XAMPP Control Panel
1. After installation, open **XAMPP Control Panel**
2. Click **Start** next to **Apache** (optional, not needed for this project)
3. Click **Start** next to **MySQL** (required)
   - Status should show **Running** in green
   - Logs will show: `MySQL started successfully`

### Step 3 — Create the Database
1. Open your browser and go to: `http://localhost/phpmyadmin/`
2. You should see the phpMyAdmin interface
3. Click **New** (left sidebar) → Enter database name: `ecommerce_db` → Click **Create**
4. The database is now ready

---

## Part 2: Configure Spring Boot for MySQL

### Step 1 — Update pom.xml
The project has been updated with:
- `spring-boot-starter-data-jpa` — ORM framework
- `mysql-connector-java` — MySQL driver

✅ **Already done** — no changes needed to pom.xml

### Step 2 — Update application.properties
The file has been configured with MySQL connection details. Check that it contains:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

✅ **Already done** — verified and configured

### Step 3 — Verify User Model is a JPA Entity
The User class has been converted to a JPA Entity with:
- `@Entity` annotation
- `@Id` and `@GeneratedValue` for auto-increment ID
- `@Column` constraints

✅ **Already done** — check the updated `model/User.java`

### Step 4 — UserRepository Created
The `UserRepository` now extends `JpaRepository<User, Long>` for CRUD operations.

✅ **Already done** — check the updated `repository/UserRepository.java`

---

## Part 3: Setup Postman

### Step 1 — Download & Install Postman
1. Go to: https://www.postman.com/downloads/
2. Download **Postman for Windows**
3. Run the installer
4. Create a free account or skip signup
5. Postman opens with a blank workspace

### Step 2 — Import Postman Collection
1. In Postman, click **File** → **Import** (or press `Ctrl+O`)
2. Choose **Upload Files** tab
3. Navigate to and select: `postman/E-Commerce-Week6.postman_collection.json`
4. Click **Import**

You should now see a collection called **E-Commerce Week 6** with all API requests pre-configured.

### Step 3 — Create Environment Variables (Optional but Recommended)
1. Click the **gear icon** (Settings) → **Environments** → **Create New**
2. Name: `Week 6 - Local`
3. Add variables:

| Variable | Initial Value | Current Value |
|---|---|---|
| `baseUrl` | `http://localhost:8080` | `http://localhost:8080` |
| `userId` | `1` | `1` |

4. Save and select this environment from the top dropdown

---

## Part 4: Run the Project

### Step 1 — Start MySQL (if not already running)
1. Open XAMPP Control Panel
2. Confirm **MySQL** shows **Running** in green

### Step 2 — Start Spring Boot App
1. Open PowerShell
2. Navigate to project:
   ```powershell
   cd 'c:\Users\KaranMaheshwari\Desktop\Assignment\E-Commerce'
   ```
3. Run the app:
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```
4. Wait for the message: `Started DemoApplication in X.XXXs`

### Step 3 — Test with Postman
1. Open Postman
2. In the **E-Commerce Week 6** collection, expand **User CRUD**
3. Run the requests in this order:
   - **POST Create User** — creates a new user in the MySQL database
   - **GET All Users** — retrieves all users
   - **GET User by ID** — fetch a specific user
   - **PUT Update User** — modify user details
   - **DELETE User** — remove a user

All data now persists in MySQL instead of JSON!

---

## Part 5: Verify Database

### Method 1: PHPMyAdmin
1. Open: `http://localhost/phpmyadmin/`
2. Click **ecommerce_db** (left sidebar)
3. Click **users** table
4. You should see all the users you created via the API

### Method 2: Check Hibernate SQL Logs
1. Look at the console where the Spring Boot app is running
2. You should see SQL queries like:
   ```sql
   INSERT INTO users (name, email) VALUES (?, ?)
   SELECT * FROM users
   ```

This confirms JPA is executing real SQL against MySQL.

---

## Troubleshooting

| Problem | Solution |
|---|---|
| MySQL not starting in XAMPP | Right-click XAMPP → Run as Administrator → Try Start again |
| Port 3306 already in use | Another app is using MySQL port. Check `netstat -ano` in PowerShell |
| `No database selected` error | Verify `ecommerce_db` exists in phpMyAdmin; if not, create it |
| `Could not connect to database` | Verify MySQL is running, credentials are correct in `application.properties` |
| No tables created in database | Check Spring Boot console for errors; ensure `ddl-auto=update` is set |
| Postman collection not found | Use the `.postman_collection.json` file in the `postman/` folder (created during setup) |

---

## API Endpoints Summary

### GET /users
Retrieve all users from the database.

### GET /users/{id}
Retrieve a specific user by UUID.

### POST /users
Create a new user. Body:
```json
{
  "name": "John Doe",
  "email": "john@example.com"
}
```

### PUT /users/{id}
Update an existing user. Body:
```json
{
  "name": "Jane Doe",
  "email": "jane@example.com"
}
```

### DELETE /users/{id}
Delete a user by ID.

---

## Next Steps

Now that you have a real database:
- Week 7: Add authentication with JWT and Spring Security
- Week 8: Build the complete e-commerce backend with Products, Cart, and Orders

All your user data will persist across app restarts, and you can manage it via the database directly.


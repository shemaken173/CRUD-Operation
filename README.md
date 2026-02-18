# RESTful API Assignment - E-Commerce Product Management

A complete Spring Boot REST API for managing product inventory with full CRUD operations, PostgreSQL integration, and bulk data import capabilities.

## Project Overview

This project demonstrates a production-ready microservice for e-commerce product management. It implements all core CRUD operations (Create, Read, Update, Delete) with proper layering (Controller → Service → Repository) and database persistence using PostgreSQL and Hibernate ORM.

## Technologies & Stack

### Core Framework
- **Spring Boot 4.0.2** - Web framework for building REST APIs
- **Java 17** - Programming language (LTS version)
- **Maven 3.x** - Build automation and dependency management

### Database & ORM
- **PostgreSQL 18** - Production-grade relational database
- **Hibernate 7.2.1** - Object-Relational Mapping (ORM)
- **Spring Data JPA** - Data access layer abstraction

### Additional Libraries
- **Spring Web (spring-boot-starter-web)** - REST controller support and Tomcat embedded server
- **PostgreSQL JDBC Driver 42.7.8** - Database connectivity
- **HikariCP** - Connection pooling for database optimization

### Development & Testing Tools
- **Postman** - API testing and manual integration testing
- **PowerShell** - Scripting for automated tests
- **curl** - Command-line HTTP testing

## Project Structure

```
restfullApiAssignment/
├── src/
│   ├── main/
│   │   ├── java/auca/ac/rw/restfullApiAssignment/
│   │   │   ├── RestfullApiAssignmentApplication.java       # Spring Boot entry point
│   │   │   ├── controller/ProductController.java           # HTTP endpoints (REST interface)
│   │   │   ├── service/ProductService.java                 # Business logic layer
│   │   │   ├── repository/ProductRepository.java           # Data access layer (JPA)
│   │   │   └── modal/Product.java                          # Entity model (JPA entity)
│   │   └── resources/
│   │       └── application.properties                       # Spring Boot configuration
│   └── test/                                                 # (Empty - no unit tests added)
├── scripts/
│   ├── post_products.ps1                                    # PowerShell script to bulk insert products
│   └── test_crud.ps1                                        # Complete CRUD test suite
├── products.json                                            # Sample product data (10 items)
├── pom.xml                                                  # Maven dependency configuration
├── mvnw / mvnw.cmd                                          # Maven wrapper (Windows)
└── README.md                                                # This file
```

## Architecture

### Layered Architecture Pattern

```
┌─────────────────────────────────────────┐
│   HTTP Client (Postman / curl / Browser) │
└──────────────┬──────────────────────────┘
               │ HTTP Requests/Responses
               ▼
┌─────────────────────────────────────────┐
│   ProductController (REST Endpoints)    │ → Handles HTTP, routing, responses
└──────────────┬──────────────────────────┘
               │ Method calls
               ▼
┌─────────────────────────────────────────┐
│   ProductService (Business Logic)       │ → Handles validation, transactions
└──────────────┬──────────────────────────┘
               │ Data operations
               ▼
┌─────────────────────────────────────────┐
│   ProductRepository (Data Access)       │ → JPA queries, database operations
└──────────────┬──────────────────────────┘
               │ SQL via Hibernate
               ▼
┌─────────────────────────────────────────┐
│   PostgreSQL Database                    │ → Persistent data storage
└─────────────────────────────────────────┘
```

### Entity Model

**Product** (JPA Entity mapped to `product` table):
- `id` (Long, Primary Key) - Unique product identifier
- `name` (String) - Product name
- `description` (String) - Detailed product description
- `price` (Double) - Product price in currency
- `category` (String) - Product category classification
- `stockQuantity` (Integer) - Available inventory count

## API Endpoints

### 1. **Create Product (Single)**
```
POST /api/products/addProduct
Content-Type: application/json

Request Body:
{
  "id": 1001,
  "name": "Wireless Bluetooth Headphones",
  "description": "Premium noise-cancelling headphones",
  "price": 89.99,
  "category": "Electronics",
  "stockQuantity": 150
}

Response (201 Created):
"Product saved successfully."

Error (409 Conflict):
"Product with id 1001 already exists."
```

### 2. **Create Products (Bulk)**
```
POST /api/products/bulk
Content-Type: application/json

Request Body: JSON array of products
[
  { "id": 1001, "name": "Product 1", ... },
  { "id": 1002, "name": "Product 2", ... }
]

Response (201 Created):
Array of all created products with their details
```

### 3. **Read All Products**
```
GET /api/products
Accept: application/json

Response (200 OK):
[
  { "id": 1001, "name": "Product 1", "price": 89.99, ... },
  { "id": 1002, "name": "Product 2", "price": 12.50, ... }
]
```

### 4. **Read Single Product**
```
GET /api/products/{id}
Accept: application/json

Response (200 OK):
{ "id": 1001, "name": "Product 1", "price": 89.99, ... }

Error (404 Not Found):
"Product not found."
```

### 5. **Update Product**
```
PUT /api/products/{id}
Content-Type: application/json

Request Body:
{
  "name": "Updated Name",
  "description": "Updated description",
  "price": 99.99,
  "category": "Updated Category",
  "stockQuantity": 200
}

Response (200 OK):
"Product updated successfully."

Error (404 Not Found):
"Product not found."
```

### 6. **Delete Product**
```
DELETE /api/products/{id}

Response (200 OK):
"Product deleted successfully."

Error (404 Not Found):
"Product not found."
```

## Configuration

### Database Connection (application.properties)

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_db
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

**Configuration Explanation:**
- `ddl-auto=create` - Automatically creates the `product` table on startup
- `show-sql=true` - Logs all SQL queries to console (useful for debugging)
- Database: `ecommerce_db` @ `localhost:5432`
- Default credentials: `postgres` / `postgres`

## How to Run

### Prerequisites

1. **PostgreSQL installed and running**
   ```bash
   # Check if PostgreSQL is running
   netstat -ano | findstr 5432
   ```

2. **Java 17+ installed**
   ```bash
   java -version
   ```

3. **Maven installed** (or use the included Maven wrapper `mvnw.cmd`)

### Step 1: Create Database

```powershell
# Connect to PostgreSQL
psql -h localhost -U postgres

# In psql shell:
CREATE DATABASE ecommerce_db;
\q
```

### Step 2: Build the Project

```bash
cd c:\Users\USER\Downloads\setup\restfullApiAssignment
.\mvnw.cmd clean package -DskipTests
```

### Step 3: Run the Application

```bash
.\mvnw.cmd spring-boot:run
```

**Expected Output:**
```
Tomcat started on port 8080 (http)
Database JDBC URL [jdbc:postgresql://localhost:5432/ecommerce_db]
HikariPool-1 - Start completed
```

The API is now available at: `http://localhost:8080/api/products`

### Step 4: Load Sample Data (Optional)

```bash
# Run PowerShell script to insert 10 sample products
powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\post_products.ps1
```

Or use Postman to POST the `products.json` file to `/api/products/bulk`.

## Testing

### Automated Test Suite

Run all CRUD operations with verification:

```bash
powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\test_crud.ps1
```

**Test Output Example:**
```
=== CRUD TEST SUITE ===

1. CREATE - POST new product
[PASS] CREATE: Product saved successfully.

2. READ ALL - GET /api/products
[PASS] READ ALL: Found 11 total products

3. READ BY ID - GET /api/products/9999
[PASS] READ BY ID: Retrieved 'Test Product' - Price: 99.99

4. UPDATE - PUT /api/products/9999
[PASS] UPDATE: Product updated successfully.

5. DELETE - DELETE /api/products/9999
[PASS] DELETE: Product deleted successfully.

6. VERIFY DELETE
[PASS] VERIFY DELETE: Product confirmed deleted (404 Not Found)

[SUCCESS] ALL CRUD TESTS PASSED
```

### Manual Testing with Postman

1. **Import collection** (if available) or create requests manually
2. **Test each endpoint:**
   - POST `/api/products/addProduct` - Create single product
   - POST `/api/products/bulk` - Create multiple products
   - GET `/api/products` - List all
   - GET `/api/products/1001` - Get by ID
   - PUT `/api/products/1001` - Update
   - DELETE `/api/products/1001` - Delete

3. **Verify responses** in Postman Console (View → Show Postman Console)

### Using curl from Command Line

```bash
# Create
curl -X POST http://localhost:8080/api/products/addProduct \
  -H "Content-Type: application/json" \
  -d "{\"id\":1,\"name\":\"Test\",\"price\":10.0,\"category\":\"Test\",\"stockQuantity\":5}"

# Read all
curl http://localhost:8080/api/products

# Read one
curl http://localhost:8080/api/products/1

# Update
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Updated\",\"price\":20.0,\"category\":\"Test\",\"stockQuantity\":10}"

# Delete
curl -X DELETE http://localhost:8080/api/products/1
```

## Key Features Implemented

✅ **CRUD Operations** - Full Create, Read, Update, Delete functionality  
✅ **RESTful API** - Proper HTTP methods and status codes  
✅ **Database Persistence** - PostgreSQL with Hibernate ORM  
✅ **Layered Architecture** - Controller → Service → Repository separation  
✅ **Error Handling** - Proper exception handling and HTTP responses  
✅ **Bulk Operations** - Single endpoint to insert multiple products  
✅ **Data Validation** - Duplicate ID detection on creation  
✅ **Automated Testing** - Complete test suite for all operations  
✅ **Connection Pooling** - HikariCP for optimized database connections  

## Dependencies (Maven)

```xml
<!-- Core Framework -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
  <version>4.0.1</version>
</dependency>

<!-- Database -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-jpa</artifactId>
  <version>4.0.1</version>
</dependency>

<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <version>42.7.8</version>
</dependency>
```

All dependencies are managed by Spring Boot's parent POM (version 4.0.2).

## Common Issues & Solutions

### Issue: Connection Refused (port 8080)

**Solution:** Another application is using port 8080
```bash
netstat -ano | findstr 8080
taskkill /PID <PID> /F
```

### Issue: Database Connection Failed

**Solution:** PostgreSQL not running or wrong credentials
```bash
# Verify PostgreSQL
psql -h localhost -U postgres -d ecommerce_db

# Check connection string in application.properties
```

### Issue: 400 Bad Request on POST

**Solution:** Invalid JSON in request body
- Ensure proper double quotes (not single)
- Check `Content-Type: application/json` header
- Validate JSON syntax using JSONLint or Postman

### Issue: Table Not Created

**Solution:** Set `spring.jpa.hibernate.ddl-auto=create` and restart
- Restart the application
- Check logs for creation SQL statements

## Files Modified/Created

### Core Implementation
- `ProductController.java` - Added GET/PUT/DELETE endpoints + bulk endpoint
- `ProductService.java` - Added read/update/delete/saveAll methods
- `Product.java` - JPA entity with all fields
- `ProductRepository.java` - JPA interface (auto-implemented)

### Configuration
- `application.properties` - PostgreSQL connection details

### Supporting Files
- `products.json` - Sample product data (10 items)
- `scripts/post_products.ps1` - Bulk data insertion script
- `scripts/test_crud.ps1` - Comprehensive test suite
- `pom.xml` - Maven dependencies (no changes needed)

## Next Steps / Enhancements (Optional)

- Add pagination for large result sets: `findAll(Pageable)`
- Add search/filter: `@Query` annotations in repository
- Add authentication/authorization: Spring Security
- Add input validation: `@Valid`, `@NotNull`, etc.
- Add logging: SLF4J with Logback
- Add unit tests: JUnit 5, MockMvc
- Add API documentation: Swagger/OpenAPI
- Add caching: Redis for frequently accessed products
- Add transactions: `@Transactional` for complex operations
- Deploy to cloud: Docker + Kubernetes / Azure / AWS

## Summary

This project demonstrates a **production-ready REST API** with:
- Industry-standard layered architecture
- Proper separation of concerns (Controller, Service, Repository)
- Database persistence with ORM
- Full CRUD functionality with error handling
- Automated testing capability
- PostgreSQL integration

The API is fully functional and can handle real-world e-commerce product management scenarios.

---

**Created:** February 18, 2026  
**Project Status:** Complete ✓  
**All Tests Passing:** Yes ✓

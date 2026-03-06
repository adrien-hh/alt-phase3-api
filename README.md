# Internal Tools API

REST API for managing internal SaaS tools used across departments.

The API supports:

- tool creation and update
- filtering, pagination and sorting
- usage metrics (last 30 days)
- consistent validation and error handling
- OpenAPI documentation

---

## Technologies

- Language: **Java 21**
- Framework: **Spring Boot**
- Data access: **Spring Data JPA / Hibernate**
- Database: **MySQL**
- Build tool: **Gradle**
- API documentation: **OpenAPI / Swagger (springdoc)**
- Validation: **Jakarta Validation**
- Logging: **SLF4J / Logback**
- Connection pool: **HikariCP**
- API port: **8081 (configurable)**

---

## Quick Start

### 1. Start database

```bash
docker-compose --profile mysql up -d
````

### 2. Install dependencies

```bash
./gradlew build
```

### 3. Run the API

```bash
./gradlew bootRun
```

### 4. API available at

```
http://localhost:8081/api/tools
```

### 5. Swagger documentation

```
http://localhost:8081/api/docs
```

---

## Configuration

Configuration is externalized through `application.properties`.

Key parameters:

```
server.port=8081
spring.datasource.url=jdbc:mysql://localhost:3306/tools_db
spring.datasource.username=...
spring.datasource.password=...
```

Database configuration can be adjusted through environment variables or `.env`.

Connection pooling is handled using **HikariCP** (default Spring Boot pool).

Example configuration:

```
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=2
```

---

## Tests

Run all tests with:

```bash
./gradlew test
```

Test coverage includes:

* **Controller tests** (`@WebMvcTest`)
* **Service unit tests** (Mockito)
* **Repository tests** (`@DataJpaTest` with H2)

Repository tests are limited to **usage metrics queries**, which contain custom SQL logic.

---

## Architecture

The application follows a layered architecture:

```
Controller → Service → Repository
```

Responsibilities:

* **Controller**

    * request handling
    * validation
    * HTTP responses

* **Service**

    * business logic
    * orchestration of repository calls
    * DTO ↔ entity transformations

* **Repository**

    * persistence layer
    * Spring Data JPA queries

---

## Project Structure

```
controller/
service/
repository/
domain/
dto/
exception/
specification/
```

* **domain**

    * JPA entities and enums

* **dto**

    * request/response models used by the API

* **specification**

    * dynamic filtering using JPA Specifications

* **exception**

    * centralized error handling (`@RestControllerAdvice`)

---

## API Features

### CRUD operations

```
GET    /api/tools
GET    /api/tools/{id}
POST   /api/tools
PUT    /api/tools/{id}
```

### Filtering

Supported filters:

* `department`
* `status`
* `category`
* `min_cost`
* `max_cost`

### Pagination

```
?page=0&limit=20
```

### Sorting

```
?sort=name,asc
?sort=cost,desc
?sort=date,desc
```

---

## Error Handling

Errors follow a consistent JSON structure:

```json
{
  "error": "VALIDATION_ERROR",
  "message": "Validation failed",
  "details": {
    "name": "Name is required"
  }
}
```

Handled HTTP codes include:

* `400` validation errors
* `404` resource not found
* `415` unsupported media type
* `500` internal server error

---

## Logging

Application logging uses **SLF4J**.

Key events logged:

* tool creation
* tool updates
* tool retrieval
* unexpected server errors

Logs help trace API usage and debugging without exposing sensitive data.

---

## Future improvements

The current implementation focuses on core CRUD operations and filtering capabilities.

Future improvements could include the analytics layer described in Part 2 of the exercise:

### Analytics endpoints

- `GET /api/analytics/department-costs` : Department cost distribution
- `GET /api/analytics/expensive-tools` : Most expensive tools analysis
- `GET /api/analytics/tools-by-category` : Tools distribution by category
- `GET /api/analytics/low-usage-tools` : Low usage tools detection
- `GET /api/analytics/vendor-summary` : Vendor cost analysis

These endpoints would provide aggregated metrics to support cost optimization and data-driven decision making.

### Potential technical improvements

- optimized SQL aggregation queries for analytics
- caching of analytics results (Redis)
- scheduled jobs for precomputed reporting
- extended monitoring and metrics
- role-based access for analytics dashboards
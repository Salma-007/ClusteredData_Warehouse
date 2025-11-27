# FX Deals Warehouse API

A robust RESTful API built with Spring Boot for managing Foreign Exchange (FX) deal transactions. This system validates, persists, and manages FX deals with comprehensive error handling and duplicate prevention.

## ✨ Features

* **Batch Import** - Import multiple deals in a single request
* **Individual Deal Management** - Create and retrieve individual deals
* **Duplicate Prevention** - Automatically detects and skips duplicate deals
* **Data Validation** - Validates all fields before persistence
* **No Rollback** - Valid deals are saved even if some fail
* **PostgreSQL Persistence** - Reliable database storage
* **Dockerized Setup** - Ready-to-run with `docker-compose.yml`
* **Global Exception Handling** - Consistent error responses
* **Swagger Documentation** - Interactive API documentation

---

## 📋 API Endpoints

### 1. Import Deals (Batch)
**`POST /api/deals/import`**

Import multiple deals at once. Valid deals are saved, invalid or duplicate deals are skipped.

**Request Example:**

```json
[
  {
    "dealUniqueId": "FX001",
    "fromCurrencyIsoCode": "USD",
    "toCurrencyIsoCode": "EUR",
    "dealTimestamp": "2024-11-27T10:30:00",
    "dealAmount": 100000.50
  },
  {
    "dealUniqueId": "FX002",
    "fromCurrencyIsoCode": "GBP",
    "toCurrencyIsoCode": "USD",
    "dealTimestamp": "2024-11-27T11:15:00",
    "dealAmount": 250000.75
  }
]
```

**Response Example:**

```json
{
  "imported": 2,
  "skipped": 0,
  "errors": []
}
```

**Responses:**
- `200 OK` – Import completed (check summary for details)
- `400 Bad Request` – Validation errors or empty list

---

### 2. Get All Deals
**`GET /api/deals`**

Retrieve all persisted deals from the database.

**Response Example:**

```json
[
  {
    "id": 1,
    "dealUniqueId": "FX001",
    "fromCurrencyIsoCode": "USD",
    "toCurrencyIsoCode": "EUR",
    "dealTimestamp": "2024-11-27T10:30:00",
    "dealAmount": 100000.50,
    "createdAt": "2024-11-27T10:35:00Z"
  }
]
```

**Responses:**
- `200 OK` – List of deals (empty array if none)

---

## 🔐 Validation Rules

| Field | Rules |
|-------|-------|
| `dealUniqueId` | Required, non-empty, max 100 characters, unique |
| `fromCurrencyIsoCode` | Required, exactly 3 uppercase letters (e.g., USD) |
| `toCurrencyIsoCode` | Required, exactly 3 uppercase letters (e.g., EUR) |
| `dealTimestamp` | Required, valid ISO 8601 date-time |
| `dealAmount` | Required, positive decimal, max 15 digits + 4 decimals |

---

## 🧪 Testing

The project includes comprehensive testing:

- **Unit Tests** - Service layer with Mockito
- **Integration Tests** - Full API testing with Testcontainers
- **Test Coverage** - 80%+ code coverage with JaCoCo
- **Test Cases:**
  - Valid deal imports
  - Duplicate detection and handling
  - Validation error scenarios
  - Partial import without rollback

**Run Tests:**

```bash
# Using PowerShell script
.\build.ps1 test

# Using Maven directly
.\mvnw.cmd test

# With coverage report
.\mvnw.cmd test jacoco:report
# View: target/site/jacoco/index.html
```

---

## 🐳 Dockerized Setup

### Prerequisites
- Docker Desktop installed
- Git

### 1. Clone the Project

```bash
git clone https://github.com/Salma-007/ClusteredData_Warehouse
cd ClusteredData_Warehouse
```

### 2. Environment Configuration

The application uses these default credentials (configurable in `docker-compose.yml`):

```yaml
POSTGRES_DB: fxdeals
POSTGRES_USER: fxuser
POSTGRES_PASSWORD: fxpassword
```

### 3. Run the Application

```bash
# Start all services
docker-compose up --build -d

# View logs
docker-compose logs -f fx-deals-app

# Stop services
docker-compose down
```

### 4. Access the Application

- **API Base URL:** `http://localhost:8080/api/deals`

---

## 📝 Sample Requests

### Create Valid Deals

```http
POST http://localhost:8080/api/deals/import
Content-Type: application/json

[
  {
    "dealUniqueId": "FX001",
    "fromCurrencyIsoCode": "USD",
    "toCurrencyIsoCode": "EUR",
    "dealTimestamp": "2024-11-27T10:30:00",
    "dealAmount": 100000.50
  },
  {
    "dealUniqueId": "FX002",
    "fromCurrencyIsoCode": "GBP",
    "toCurrencyIsoCode": "USD",
    "dealTimestamp": "2024-11-27T11:15:00",
    "dealAmount": 250000.75
  }
]
```

### Duplicate Deal (Should Skip)

```http
POST http://localhost:8080/api/deals/import
Content-Type: application/json

[
  {
    "dealUniqueId": "FX001",
    "fromCurrencyIsoCode": "USD",
    "toCurrencyIsoCode": "GBP",
    "dealTimestamp": "2024-11-27T13:00:00",
    "dealAmount": 2000.00
  }
]
```

**Expected Response:**
```json
{
  "imported": 0,
  "skipped": 1,
  "errors": [
    {
      "dealUniqueId": "FX001",
      "reason": "Duplicate entry"
    }
  ]
}
```

### Invalid Input (Validation Errors)

```http
POST http://localhost:8080/api/deals/import
Content-Type: application/json

[
  {
    "dealUniqueId": "",
    "fromCurrencyIsoCode": "US",
    "toCurrencyIsoCode": "EURO",
    "dealTimestamp": null,
    "dealAmount": -100
  }
]
```

**Expected Response:** `400 Bad Request` with validation details

### Fetch All Deals

```http
GET http://localhost:8080/api/deals
```

---

## 💻 Local Development

### Prerequisites
- Java 17
- Maven 3.6+
- PostgreSQL (or use Docker)

### Setup

1. **Configure Database** (if not using Docker)

Update `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/fxdeals
    username: your_username
    password: your_password
```

2. **Run Application**

**Using PowerShell Script:**
```powershell
# Compile
.\build.ps1 build

# Run tests
.\build.ps1 test

# Run application
.\build.ps1 run
```

**Using Maven Directly:**
```bash
# Compile
.\mvnw.cmd clean compile

# Run tests
.\mvnw.cmd test

# Run application
.\mvnw.cmd spring-boot:run
```

**Using IntelliJ IDEA:**
- Open Maven view (right panel)
- Navigate to: Lifecycle → spring-boot:run
- Double-click to run

3. **Access Application**
- API: `http://localhost:8080/api/deals`

---

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/com/example/datawarehouse/
│   │   ├── DatawarehouseApplication.java
│   │   ├── controller/
│   │   │   └── FxDealController.java
│   │   ├── dto/
│   │   │   ├── request/
│   │   │   │   └── FxDealRequest.java
│   │   │   └── response/
│   │   │       ├── ErrorResponse.java
│   │   │       ├── FxDealResponse.java
│   │   │       └── ImportSummaryResponse.java
│   │   ├── exception/
│   │   │   ├── ControllerExceptionHandler.java
│   │   │   └── DuplicateDealException.java
│   │   ├── mapper/
│   │   │   └── FxDealMapper.java
│   │   ├── model/
│   │   │   └── FxDeal.java
│   │   ├── repository/
│   │   │   └── FxDealRepository.java 
│   │   └── service/
│   │       └── FxDealService.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/example/datawarehouse/
        ├── controller/
        └── service/
```

---

## 🔑 Key Design Decisions

### 1. **Independent Transactions**
Each deal is saved in its own transaction (`@Transactional(propagation = REQUIRES_NEW)`). This ensures valid deals are persisted even if others fail - no rollback.

### 2. **Validation Strategy**
- **Bean Validation** - Jakarta Validation annotations (`@Valid`)
- **Business Logic Validation** - Duplicate checks in service layer
- **Database Constraints** - Unique index on `dealUniqueId`

### 3. **Error Handling**
- Global exception handler (`@RestControllerAdvice`)
- Structured error responses with meaningful messages
- Different HTTP status codes for different error types

### 4. **Clean Architecture**
- Clear separation: Controller → Service → Repository
- DTOs for API contracts
- Mapper component for entity-DTO conversion
- Entities isolated from API layer


---

## 📊 Important Notes

✅ **Duplicate Prevention** - Uses unique ID check before saving  
✅ **No Rollback** - All valid deals are persisted independently  
✅ **Batch Processing** - Processes multiple deals in one request  
✅ **Global Error Handling** - Consistent error responses  
✅ **Clean DTOs** - Clear API contract separation

---

## 🛠️ Technologies

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Programming Language |
| Spring Boot | 3.2.0 | Application Framework |
| Spring Data JPA | 3.2.0 | Data Access Layer |
| PostgreSQL | 15 | Database |
| Lombok | 1.18.30 | Reduce Boilerplate |
| Jakarta Validation | 3.0 | Bean Validation |
| SpringDoc OpenAPI | 2.3.0 | API Documentation |
| Docker | Latest | Containerization |
| JUnit 5 | 5.10.0 | Testing Framework |
| Mockito | 5.5.0 | Mocking Framework |
| Testcontainers | 1.19.3 | Integration Testing |
| JaCoCo | 0.8.11 | Code Coverage |
| Maven | 3.9.11 | Build Tool |

---


## Troubleshooting

### Application won't start
```bash
# Check if port 8080 is free
netstat -ano | findstr :8080

# Check Docker containers
docker-compose ps

# View logs
docker-compose logs fx-deals-app
```



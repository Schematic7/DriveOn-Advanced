# 🚗 DriveOn Motors – Enterprise Auto Service Management System

DriveOn Motors is a web-based vehicle management and auto service appointment scheduling system. The application provides a user-friendly interface allowing clients to manage their virtual garage, while administrators can easily track and process service requests. The project is built as part of the **Spring Advanced Course @ SoftUni (June 2026)** and strictly follows modern layered architecture, clean code principles, SOLID guidelines, and distributed microservice communication.

---

## 🏗️ Project Architecture

The solution is implemented as a distributed system consisting of a core Main Spring Boot Application and an independent REST Microservice, running on separate ports with their own dedicated databases:
```text
+-------------------------------------------------------------+
|                     CLIENT BROWSER                          |
|         (HTML5 / Bootstrap 5 / Thymeleaf SPA-like UI)       |
+-------------------------------------------------------------+
                               |
                               | HTTPS / MVC
                               v
+-------------------------------------------------------------+
|                 DRIVEON MAIN APPLICATION                    |
|             (Spring Boot 3.4.0 - Port 8080)                 |
|                                                             |
|   - Core Domain Logic (Users, Vehicles, Appointments)       |
|   - Spring Security (Session-based Auth & Roles)            |
|   - Feign Client Integration (REST Communication)           |
|   - Spring Events & AOP Performance Logging                 |
|   - Caching (Spring Cache) & Cron Scheduling                |
+-------------------------------------------------------------+
       |                                              |
       | REST / JSON (OpenFeign + API Key)            | JPA / Hibernate
       v                                              v
+-------------------------------------------------+   +---------------+
|          LOYALTY REST MICROSERVICE              |   | MySQL Database|
|       (Spring Boot 3.4.0 - Port 8081)           |   | (autoservice) |
|                                                 |   +---------------+
|   - Dedicated Loyalty Account Management        |
|   - Point Accumulation & Spend API              |
|   - Secured via API-Key Token Interceptor       |
+-------------------------------------------------+
       |
       | JPA / Hibernate
       v
+---------------+
| MySQL Database|
|   (loyalty)   |
+---------------+

---

# 🛠️ Technology Stack

| Category             | Technology                                    |
| -------------------- | --------------------------------------------- |
| **Language**         | Java 17                                       |
| **Framework**        | Spring Boot 3.4.0                             |
| **Build Tool**       | Apache Maven                                  |
| **Database**         | MySQL 8.0                                     |
| **Persistence**      | Spring Data JPA / Hibernate                   |
| **Security**         | Spring Security (RBAC), Custom API-Key Filter |
| **Microservices**    | Spring Cloud OpenFeign                        |
| **Frontend**         | Thymeleaf, Bootstrap 5, Font Awesome 6        |
| **Caching**          | Spring Cache                                  |
| **Scheduling**       | Spring Scheduler (Cron & Fixed Rate)          |
| **Containerization** | Docker & Docker Compose                       |

---

# 🌟 Supported Features

## 🚘 Vehicle Management

* Register vehicles in a personal garage.
* Edit existing vehicle information.
* Delete vehicles.
* Store:

    * Make
    * Model
    * Production Year
    * License Plate
    * VIN Number

---

## 📅 Service Appointment Management

Users can:

* Book service appointments.
* Select one of their registered vehicles.
* Choose from available service types:

    * Diagnostics
    * Regular Maintenance
    * General Repair
    * Annual Technical Inspection
* Cancel appointments while they remain in **PENDING** status.

Administrators can:

* View all appointments.
* Approve appointments.
* Complete appointments.
* Manage appointment lifecycle.

---

## 👤 User & Role Management

Administrators can:

* Promote users to the **ADMIN** role.
* Demote administrators back to **USER**.
* Manage all registered users through a dedicated administration panel.

---

# 🎁 Loyalty REST Microservice

The loyalty system is implemented as a standalone REST microservice.

### Features

* Automatic loyalty point accumulation.
* Loyalty point redemption for discounts.
* Real-time loyalty balance synchronization.
* Secure API communication using API Key authentication.

### Business Flow

When an administrator marks an appointment as **COMPLETED**:

* the main application sends a REST request via OpenFeign;
* the loyalty service calculates earned points;
* the customer's loyalty account is updated automatically.

During booking, customers may redeem accumulated points (e.g. **20+ points**) to receive a discount on the selected service.

---

# 🚀 Enterprise Architecture & Integrations

## Spring Events

Implemented custom domain events:

* `AppointmentCreatedEvent`
* `AppointmentNotificationListener`

Business logic remains decoupled from notification and audit functionality.

---

## Aspect-Oriented Programming (AOP)

Implemented a custom `PerformanceAspect` that:

* measures execution time;
* logs slow operations;
* monitors service-layer performance.

---

## Spring Cache

Caching is applied to frequently accessed read-only data, such as:

* Service Types
* Static catalog data

This significantly reduces unnecessary database queries.

---

## Scheduled Jobs

Implemented scheduled maintenance tasks using:

* Cron Expressions
* Fixed Delay

Example:

* `AppointmentCleanupScheduler`

---

## Dockerized Environment

The project is fully containerized.

Using the root-level `docker-compose.yml`, a single command starts:

* Main Application
* Loyalty Microservice
* MySQL Database (Main)
* MySQL Database (Loyalty)

---

# 🎨 Frontend & User Experience

The frontend is built using **Thymeleaf Server-Side Rendering** while providing a modern **Single Page Application (SPA)-like** experience.

### UI Highlights

* Bootstrap 5
* Font Awesome 6
* Responsive Design
* Unified Dark Theme
* External CDN optimization

The application contains more than **10 fully developed pages**, including:

* Home Page (Guest & Logged-in Dashboard)
* User Login
* User Registration
* Vehicle Registration (Add Vehicle)
* Vehicle Editor (Edit Vehicle)
* Service Booking Form (Add Appointment)
* User Profile View
* User Profile Editor
* Admin: All Service Appointments Management
* Admin: User & Role Management
* Custom Error Page

---

# 🚀 Local Setup

## Option A — Docker (Recommended)

Clone the repository:

```bash
git clone <YOUR_REPOSITORY_URL>
cd <repository-folder>
```

Start the entire environment:

```bash
docker-compose up --build
```

Applications will be available at:

Main Application

```
http://localhost:8080
```

Loyalty REST Service

```
http://localhost:8081
```

---

## Option B — Manual IDE Setup

Clone the repository and import both projects:

* driveon-main-app
* loyalty-service

Requirements:

* Java 17
* Maven
* MySQL 8.0

Configure the database credentials inside:

```
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/autoservice_db?createDatabaseIfNotExist=true
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

Start the applications in the following order:

1. LoyaltyServiceApplication (Port 8081)
2. AutoserviceApplication (Port 8080)

---

# 🗄️ Seed Data

If the database is empty, `DataSeeder` automatically populates sample data.

## Administrator

**Username**

```
admin
```

**Password**

```
12345
```

Permissions:

* User Management
* Role Management
* Appointment Approval
* Appointment Completion

---

## Standard User

**Username**

```
user
```

**Password**

```
12345
```

Permissions:

* Personal Garage
* Vehicle Management
* Service Booking
* Loyalty Points

---

## Predefined Service Types

| Service                     |   Price |
| --------------------------- |--------:|
| Diagnostics                 |  $50.00 |
| Regular Maintenance         | $100.00 |
| General Repair              |       0 |
| Annual Technical Inspection |  $60.00 |

---

# 🧪 Testing & Quality Assurance

Both microservices include extensive automated testing with **over 70% line coverage**.

## Unit Tests

Examples:

* VehicleMapperTest
* AppointmentMapperTest
* VehicleServiceTest

Focus:

* Business Logic
* Mappers
* Services

---

## Integration Tests

Examples:

* UserServiceIntegrationTest
* LoyaltyServiceIntegrationTest

Coverage:

* Spring Context
* Repository Layer
* Database Persistence

---

## MVC & REST API Tests

Examples:

* AdminControllerTest
* AppointmentControllerTest
* LoyaltyControllerTest

Coverage:

* HTTP Endpoints
* Authentication & Authorization
* Controller Logic
* REST Responses

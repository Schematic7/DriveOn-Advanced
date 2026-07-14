# DriveOn Motors (AutoService)

DriveOn Motors is a web-based vehicle management and auto service appointment scheduling system. The application provides a user-friendly interface allowing clients to manage their virtual garage, while administrators can easily track and process service requests.

## 🛠 Tech Stack

**Backend:**
* Java 17
* Spring Boot 3.4.0
* Spring Security (Session-based authentication and authorization)
* Spring Data JPA / Hibernate
* MySQL (Relational Database)

**Frontend:**
* HTML5 / CSS3
* Thymeleaf (Server-side rendering)
* Bootstrap 5 (Responsive design)
* FontAwesome (Iconography)

## 🌟 Core Features

The system is structured around several access levels:

**Unregistered Users (Guests):**
* Browse the home page and available services.
* Register a new account.
* Log in to the system.

**Registered Users (Clients):**
* **My Garage (Vehicle CRUD):** Add, view, edit, and delete personal vehicles.
* **Appointments:** Book a service appointment for a specific vehicle and select a required service.
* **Appointment Management:** View appointment statuses (PENDING, APPROVED, COMPLETED, CANCELLED) and cancel upcoming (PENDING) appointments.

**Administrators:**
* Access a secured administrative panel.
* View all service appointments made by clients.
* Approve appointments.
* Mark appointments as completed.

## 🔗 Integrations
* Relational database integration via Spring Data JPA for object persistence.
* External CDN libraries (Bootstrap, FontAwesome) for optimized user interface loading.

## 🚀 Local Setup Instructions

1. Clone the repository to your local machine:
   `git clone <YOUR_REPOSITORY_URL>`

2. Open the project in your preferred Java IDE (IntelliJ IDEA, Eclipse, etc.).

3. Configure the database connection in the `src/main/resources/application.properties` file:
   `spring.datasource.url=jdbc:mysql://localhost:3306/autoservice_db?createDatabaseIfNotExist=true`
   `spring.datasource.username=YOUR_MYSQL_USERNAME`
   `spring.datasource.password=YOUR_MYSQL_PASSWORD`

4. Run the application via the main class `AutoServiceApplication.java`.

5. Open a web browser and navigate to: `http://localhost:8080`

## 🗄️ Seed Data

Upon initial startup, if the database is empty, the system automatically generates initial data (via `CommandLineRunner`) to facilitate the testing and evaluation process.

**1. Test Accounts:**
To test the different access levels immediately after startup, you can use the following pre-configured profiles:

* **Administrator Access:**
    * Username: `admin`
    * Password: `12345`
* **User Access (Client):**
    * Username: `user`
    * Password: `12345`

**2. Services:**
To ensure the appointment booking form is fully functional, the database is initialized with the following basic services:

* Diagnostics
* Regular Maintenance
* General Repair
* Annual Technical Inspection
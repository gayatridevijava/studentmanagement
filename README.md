# 🎓 Student Management System

A full-stack web application for managing students and their enrolled courses, built with **Angular 18**, **Spring Boot 3**, and **H2 in-memory database**.

---

## 🏗️ Architecture Overview

```
student-management/
├── backend/                   # Spring Boot 3 REST API
│   └── src/main/java/com/studentmgmt/
│       ├── controller/        # REST controllers (StudentController, CourseController)
│       ├── service/           # Business logic with @Transactional
│       ├── repository/        # Spring Data JPA repositories
│       ├── model/             # JPA entities (Student, Course)
│       ├── dto/               # Request/Response DTOs
│       ├── exception/         # Custom exceptions + GlobalExceptionHandler
│       └── config/            # CORS config, data seeder
└── frontend/
    ├── index.html             # Standalone demo (no build required!)
    └── src/app/
        ├── models/            # TypeScript interfaces
        ├── services/          # StudentService, CourseService (RxJS BehaviorSubjects)
        └── components/        # StudentListComponent, CourseListComponent
```

---

## 🚀 Quick Start

### Backend (Spring Boot)

**Prerequisites:** Java 17+, Maven 3.8+

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

The API starts at **http://localhost:8080**

- H2 Console: http://localhost:8080/h2-console  
  (JDBC URL: `jdbc:h2:mem:studentdb`, user: `sa`, password: empty)

### Frontend Option 1: Standalone Demo (No Build!)

Simply open `frontend/index.html` in a browser with the backend running.  
This is a pure HTML/JS/CSS file that calls the API directly.

### Frontend Option 2: Angular 18 Dev Server

**Prerequisites:** Node.js 18+, npm

```bash
cd frontend
npm install
npm start
# Opens at http://localhost:4200
```

---

## ✨ Features Demonstrated

### CRUD Operations
- Create, Read, Update, Delete for both **Students** and **Courses**
- Many-to-many enrollment relationship managed via REST endpoints
- Real-time UI updates using RxJS `BehaviorSubject` state

### Routing (Angular)
- `/students` → Student management tab
- `/courses` → Course management tab
- Lazy-loaded components with `loadComponent`

### Multiple Tabs with Modal Confirmation
- Two main tabs: Students and Courses
- **Add/Edit modal** for forms
- **Detail view modal** showing enrolled courses/students
- **Confirmation modal** before any delete operation (with descriptive message)

### Error Handling
- Backend: `@RestControllerAdvice` global exception handler
- Custom exceptions: `ResourceNotFoundException`, `DuplicateResourceException`
- Frontend: HTTP error interception, field-level server validation errors displayed inline
- Toast notifications for success/error feedback

### Multi-Component Communication
- `StudentService` and `CourseService` use `BehaviorSubject` for shared state
- Components subscribe to `students$` and `courses$` observables
- Enrolling/unenrolling a student refreshes both components' state
- `NotificationService` uses `Subject` to broadcast toasts to `AppComponent`

### Form Handling
- Angular Reactive Forms with validators
- Client-side validation: required, email, minLength, pattern, date
- Server-side validation errors mapped back to form fields
- Visual error states (red border + error message)

### Backend CRUD Services
- `StudentService`: full CRUD + enroll/unenroll, `@Transactional`
- `CourseService`: full CRUD, cascade delete enrollment cleanup
- DTO pattern (Request/Response) with MapStruct-style manual mapping
- Duplicate detection (email, course code) with meaningful errors

### Backend Validation, Exception Handling, Transaction
- Bean Validation (`@Valid`, `@NotBlank`, `@Email`, `@Past`, `@Pattern`)
- `@Transactional(readOnly = true)` for reads, `@Transactional` for writes
- `GlobalExceptionHandler` returns structured `ApiErrorResponse` with field-level errors
- HTTP status codes: 200, 201, 204, 400, 404, 409, 500

---

## 🧪 Unit Tests

Run backend tests:
```bash
cd backend
mvn test
```

Test coverage includes:
- `StudentServiceTest` — 9 tests covering all service methods
- `CourseServiceTest` — 8 tests covering all service methods  
- `StudentControllerTest` — 7 integration tests using MockMvc

```
Tests passing:
✅ StudentServiceTest (9 tests)
✅ CourseServiceTest (8 tests)
✅ StudentControllerTest (7 tests)
Total: 24 unit tests
```

---

## 📡 API Reference

### Students API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/students` | Get all students with courses |
| GET | `/api/students/{id}` | Get student by ID |
| POST | `/api/students` | Create new student |
| PUT | `/api/students/{id}` | Update student |
| DELETE | `/api/students/{id}` | Delete student |
| POST | `/api/students/{id}/courses/{courseId}` | Enroll in course |
| DELETE | `/api/students/{id}/courses/{courseId}` | Unenroll from course |

### Courses API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/courses` | Get all courses with students |
| GET | `/api/courses/{id}` | Get course by ID |
| POST | `/api/courses` | Create new course |
| PUT | `/api/courses/{id}` | Update course |
| DELETE | `/api/courses/{id}` | Delete course |

### Sample Request Body (Student)
```json
{
  "firstName": "Alice",
  "lastName": "Johnson",
  "email": "alice@university.edu",
  "dateOfBirth": "2001-05-15",
  "phone": "+12025551234",
  "courseIds": [1, 2, 3]
}
```

### Sample Request Body (Course)
```json
{
  "name": "Introduction to Computer Science",
  "code": "CS101",
  "description": "Fundamentals of CS including algorithms and data structures",
  "credits": 3,
  "instructor": "Dr. Alan Turing"
}
```

### Error Response Format
```json
{
  "status": 400,
  "error": "Validation Failed",
  "message": "One or more fields have validation errors",
  "path": "/api/students",
  "timestamp": "2024-01-15T10:30:00",
  "validationErrors": {
    "email": "Email must be valid",
    "firstName": "First name is required"
  }
}
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Frontend Framework | Angular 18 (Standalone Components) |
| State Management | RxJS BehaviorSubject, combineLatest |
| Form Handling | Angular Reactive Forms |
| HTTP Client | Angular HttpClient + RxJS operators |
| UI Library | Custom CSS (no external UI framework) |
| Backend Framework | Spring Boot 3.2 |
| REST | Spring MVC (@RestController) |
| ORM | Spring Data JPA / Hibernate |
| Database | H2 In-Memory (production-ready for MySQL/PostgreSQL swap) |
| Validation | Bean Validation (jakarta.validation) |
| Build | Maven 3.8 |
| Testing | JUnit 5, Mockito, MockMvc |
| Utilities | Lombok |

---

## 🔄 Switching to MySQL/PostgreSQL

1. Add dependency in `pom.xml`:
```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

2. Update `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/studentdb
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
```

---

## 🌱 Seed Data

On startup, the app automatically seeds:
- **5 Courses**: CS101, MATH201, PHYS101, ENG201, CS301
- **3 Students**: Alice (3 courses), Bob (3 courses), Carol (2 courses)

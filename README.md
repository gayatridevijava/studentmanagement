# 🎓 Student Management System

Backend application (REST APIs) for managing students and their enrolled courses, built with 
 **Spring Boot 4**, and **MySQL database**.

---

## 🏗️ Architecture Overview

```
student-management/
├── backend/                   # Spring Boot 4 REST API
│   └── src/main/java/com/studentmgmt/
│       ├── controller/        # REST controllers (StudentController, CourseController)
│       ├── service/           # Business logic with @Transactional
│       ├── repository/        # Spring Data JPA repositories
│       ├── model/             # JPA entities (Student, Course, Enrollment)
│       ├── dto/               # Request/Response DTOs
│       ├── exception/         # Custom exceptions + GlobalExceptionHandler
│       └── config/            # CORS config
└── 


## 🚀 Quick Start

### Backend (Spring Boot)

**Prerequisites:** Java 17+, Maven 3.8+

```bash
mvn clean install
mvn spring-boot:run
```

The API starts at **http://localhost:8080**

- MySQL DB Scripts are src/main/resources/sql folder



## 🛠️ Tech Stack

| Layer | Technology                                                |
|-------|-----------------------------------------------------------|
| Backend Framework | Spring Boot 4                                             |
| REST | Spring MVC (@RestController)                              |
| ORM | Spring Data JPA / Hibernate                               |
| Database |MySQL |
| Validation | Bean Validation (jakarta.validation)                      |
| Build | Maven 3.8                                                 |
| Testing | JUnit 5, Mockito, MockMvc                                 |
| Utilities | Lombok                                                    |

---


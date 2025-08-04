# 📚 College Library Management System — Backend (Spring Boot + MySQL)

A secure, full-stack web application to manage a college library.  
Supports user roles (`STUDENT`, `LIBRARIAN`), book inventory, borrow/return system, fine calculation, and overdue tracking.

---

## 🚀 Tech Stack

- **Backend**: Spring Boot 3, Spring Security, JWT
- **Database**: MySQL
- **ORM**: Spring Data JPA
- **Docs**: Swagger / OpenAPI
- **Validation & Exception Handling**: Bean Validation, RestControllerAdvice

---

## 🔐 Features

### 👤 Authentication & Roles
- User Registration & Login (JWT Auth)
- Role-based access: `STUDENT`, `LIBRARIAN`

### 📚 Book Management
- Librarians can add/edit/delete books
- Students can view & search books
- Pagination & sorting supported

### 🔁 Borrow/Return System
- Students can borrow & return books
- Auto fine calculated for overdue returns
- Available copies tracked

### 📊 Borrow History & Overdue Tracking
- Students: view personal borrow history
- Librarians: view all active (unreturned) borrows
- Overdue flag included in API response

---

## 🔧 Setup Instructions

### ✅ Prerequisites:
- Java 17+
- Maven
- MySQL

### 📦 Clone & Build
```bash
git clone https://github.com/Aditya-Somasi/Library.git
cd library-backend
```

### ⚙️ Configure DB

Update src/main/resources/application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/library_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

### Run the App

```bash

./mvnw spring-boot:run

```

### App runs at:

http://localhost:8080

### 📘 API Documentation (Swagger UI)

```bash
http://localhost:8080/swagger-ui/index.html
```
### Sample API Testing (via Postman)
POST /api/auth/register — register as student or librarian

POST /api/auth/login — get JWT token

GET /api/books?page=0&size=5 — list books

POST /api/borrow/{bookId} — student borrows a book

POST /api/borrow/return/{bookId} — student returns a book

GET /api/borrow/my-history — student’s borrow history

GET /api/borrow/active — librarian sees unreturned books

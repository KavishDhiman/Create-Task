# Task Management Application

A centralized enterprise-style Task Management Application built using Spring Boot and Thymeleaf following a Monolithic Architecture.  
The application provides complete management for users, roles, projects, tasks, categories, comments, attachments, notifications, and reporting dashboards.

The system was developed collaboratively as a team project with modular ownership of APIs and UI components.

---

# Technology Stack

## Backend
- Java 17
- Spring Boot 3
- Spring MVC
- Spring Data JPA
- Hibernate
- Spring Security
- Spring Vault

## Frontend
- Thymeleaf
- HTML5
- CSS3


## Database
- MySQL

## Infrastructure
- AWS EC2
- Centralized MySQL Database on EC2
- Centralized HashiCorp Vault on EC2

## API Documentation
- Swagger / OpenAPI

---

# Architecture

The application follows a **Monolithic Architecture** where:

- Frontend and backend are deployed together
- All modules exist within a single Spring Boot application
- Shared centralized database is hosted on AWS EC2
- Secrets and credentials are managed using HashiCorp Vault
- Thymeleaf is used for server-side rendered UI

---

# Key Features

- User & Role Management
- Project Management
- Task Tracking
- Task Assignment
- Category Mapping
- Comment Management
- Attachment Management
- Notification System
- Reporting Dashboards
- Secure Credential Management using Vault
- Centralized Cloud Deployment

---

# Security Implementation

The application uses **HashiCorp Vault** for secure secret management.

Sensitive configurations such as:
- Database credentials
- Application secrets
- Environment configurations

are stored securely inside Vault instead of hardcoding them in the application.

Vault was deployed centrally on AWS EC2 and accessed securely by the Spring Boot application using Spring Cloud Vault.

---

# Deployment Infrastructure

## AWS EC2 Setup

The project uses centralized cloud infrastructure:

### EC2 Instance 1
- Spring Boot Application
- Thymeleaf Frontend

### EC2 Instance 2
- MySQL Database Server

### EC2 Instance 3
- HashiCorp Vault Server

This setup simulates an industry-style centralized deployment environment.

---

# Database Design

The application contains the following major entities:

- User
- UserRole
- UserRoles
- Project
- Task
- Category
- TaskCategory
- Comment
- Attachment
- Notification

The schema includes:
- One-to-Many relationships
- Many-to-Many mappings
- Foreign Key constraints
- Aggregation-ready structure for reporting APIs

---

# Team Structure

| Team Member | Module Responsibility |
|---|---|
| Kaviya | Users & Roles |
| Kavish | Projects |
| Jayanthi | Tasks & Categories |
| Srihari | Attachments & Comments |
| Meenakshi | Thymeleaf UI & Notifications |

---

# API Modules

---

# 1. User & Role Management APIs

## User APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/users` | Create a new user |
| GET | `/api/v1/users/{userId}` | Get user details |
| GET | `/api/v1/users` | List all users |
| PUT | `/api/v1/users/{userId}` | Update user profile |
| DELETE | `/api/v1/users/{userId}` | Delete/deactivate user |

## Role APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/roles` | Create role |
| GET | `/api/v1/roles` | List all roles |

## User–Role Mapping APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/users/{userId}/roles/{roleId}` | Assign role |
| DELETE | `/api/v1/users/{userId}/roles/{roleId}` | Remove role |
| GET | `/api/v1/users/{userId}/roles` | Get user roles |

---

# 2. Project Management APIs

## Project APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/projects` | Create project |
| GET | `/api/v1/projects/{projectId}` | Get project details |
| GET | `/api/v1/projects` | List all projects |
| PUT | `/api/v1/projects/{projectId}` | Update project |
| DELETE | `/api/v1/projects/{projectId}` | Delete/close project |

## Project Ownership APIs

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/users/{userId}/projects` | Get projects owned by user |

---

# 3. Task & Category APIs

## Task APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/tasks` | Create task |
| GET | `/api/v1/tasks/{taskId}` | Get task details |
| GET | `/api/v1/tasks` | List all tasks |
| PUT | `/api/v1/tasks/{taskId}` | Update task |
| DELETE | `/api/v1/tasks/{taskId}` | Delete task |

## Task Assignment & Filtering

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/projects/{projectId}/tasks` | Tasks under project |
| GET | `/api/v1/users/{userId}/tasks` | Tasks assigned to user |
| GET | `/api/v1/tasks/status/{status}` | Filter by status |
| GET | `/api/v1/tasks/priority/{priority}` | Filter by priority |

## Category APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/categories` | Create category |
| GET | `/api/v1/categories` | List categories |

## Task–Category Mapping APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/tasks/{taskId}/categories/{categoryId}` | Assign category |
| DELETE | `/api/v1/tasks/{taskId}/categories/{categoryId}` | Remove category |
| GET | `/api/v1/tasks/{taskId}/categories` | Get task categories |

---

# 4. Comment & Attachment APIs

## Comment APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/tasks/{taskId}/comments` | Add comment |
| GET | `/api/v1/tasks/{taskId}/comments` | View comments |
| DELETE | `/api/v1/comments/{commentId}` | Delete comment |

## Attachment APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/tasks/{taskId}/attachments` | Upload attachment |
| GET | `/api/v1/tasks/{taskId}/attachments` | List attachments |
| DELETE | `/api/v1/attachments/{attachmentId}` | Delete attachment |

---

# 5. Notification APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/notifications` | Create notification |
| GET | `/api/v1/notifications/{notificationId}` | Get notification |
| GET | `/api/v1/users/{userId}/notifications` | User notifications |
| DELETE | `/api/v1/notifications/{notificationId}` | Delete notification |

## Advanced Notification APIs

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/users/{userId}/notifications/unread` | Get unread notifications |
| PUT | `/api/v1/notifications/{notificationId}/read` | Mark notification as read |

---

# Reporting APIs

The reporting module simulates enterprise dashboard and KPI systems.

---

## 1. User Productivity Report

### Endpoint
```http
GET /api/v1/reports/users/productivity
```

### Features
- Task aggregation
- Completion percentage calculation
- KPI-style reporting
- DTO-based response modeling

### Tables Used
- User
- Task

---

## 2. Project Summary Dashboard

### Endpoint
```http
GET /api/v1/reports/projects/summary
```

### Features
- Multi-table joins
- Progress percentage calculation
- Project health dashboard
- Aggregated task statistics

### Tables Used
- Project
- Task

---

## 3. Overdue Tasks Report

### Endpoint
```http
GET /api/v1/reports/tasks/overdue?days=7
```

### Features
- Deadline tracking
- Overdue calculations
- Business rule implementation
- Filtering and date handling

### Tables Used
- Task
- User
- Project

---

# Application UI

The frontend was developed using Thymeleaf with:
- Responsive endpoint dashboard
- Team-based module navigation
- API testing interface
- Modern dark-themed UI
- Dynamic response viewer pages

---


# Swagger API Documentation

Swagger UI can be accessed at:

```http
http://localhost:8080/swagger-ui/index.html
```

---

# Learning Outcomes

This project demonstrates:
- Enterprise backend development
- REST API design
- Monolithic system architecture
- Secure secret management
- Cloud deployment concepts
- Centralized infrastructure handling
- Database relationship modeling
- Team-based development workflow

---

# Future Enhancements

- Docker containerization
- CI/CD pipeline integration
- Role-based authorization
- JWT authentication
- File storage using AWS S3
- Email notification integration
- Microservices migration

---

# Conclusion

This project simulates a real-world enterprise task management platform with centralized infrastructure, secure secret management, scalable database design, reporting dashboards, and modular API architecture.

The application was designed to demonstrate industry-standard backend engineering practices using Spring Boot, Vault, AWS EC2, and Thymeleaf.

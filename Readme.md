# Smart Curriculum Activity And Attendance

Smart Curriculum Activity And Attendance is a comprehensive, full-stack platform for managing attendance and curriculum activities in educational institutions.  
It provides role-based access for administrators, teachers, and students, supports QR-based and manual attendance, and includes PostgreSQL/Supabase migrations and Dockerized deployment for easy setup.

---

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Repository Structure](#repository-structure)
- [Technology Stack](#technology-stack)
- [Getting Started](#getting-started)
- [Environment Variables (.env.example)](#environment-variables-envexample)
- [Run with Docker Compose](#run-with-docker-compose)
- [Database Schema & Migrations](#database-schema--migrations)
- [Mermaid ER Diagram](#mermaid-er-diagram)
- [Workflows (Mermaid)](#workflows-mermaid)
  - [Attendance Flow](#attendance-flow)
  - [Curriculum / Activity Flow](#curriculum--activity-flow)
  - [User Onboarding Flow](#user-onboarding-flow)
- [API Endpoints (Examples)](#api-endpoints-examples)
- [Build, Run & Test](#build-run--test)
- [Architecture](#architecture)
- [Future Enhancements](#future-enhancements)
- [Contributing](#contributing)
- [License](#license)
- [Author](#author)

---

## Overview
This project addresses the common problems institutions face with manual attendance and fragmented activity tracking. The platform centralizes attendance, curriculum planning, and user management, enabling better reporting, auditing, and integration.

Primary goals:
- Make attendance reliable and auditable (QR + manual)
- Provide teachers easy tools to assign and monitor curriculum activities
- Keep administration controls centralized and secure
- Use migration-first database management to support deployments (Supabase/Postgres)

---

## Features
- Role-based access: Admin, Teacher, Student
- QR-based attendance and manual marking
- Attendance correction, bulk operations, and reporting
- Curriculum and activity creation, assignment, tracking, completion state
- PostgreSQL with Supabase-compatible migrations
- Docker Compose for local development and testing
- Java (Spring Boot) backend with REST APIs
- JWT-based authentication
- Test suites and CI-ready structure (Maven)

---

## Repository Structure
```
.
├── src/                        # Java backend source code (controllers, services, repositories)
├── supabase/
│   └── migrations/             # SQL migrations for PostgreSQL/Supabase
├── docker-compose.yml          # Docker Compose for local setup
├── pom.xml                     # Maven build configuration
├── mvnw, mvnw.cmd              # Maven wrapper scripts
├── LICENSE                     # MIT License
└── README.md                   # Project documentation
```

---

## Technology Stack
- Backend: Java 17+, Spring Boot, Maven  
- Database: PostgreSQL (Supabase compatible)  
- Deployment: Docker, Docker Compose  
- Auth: JWT (JSON Web Tokens)  
- Migrations: SQL files under `supabase/migrations`

Badges (copy if you want visual badges on GitHub):
```markdown
![Java](https://img.shields.io/badge/Java-17-blue?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-green?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-blue?logo=docker&logoColor=white)
```

---

## Getting Started

### Prerequisites
- Java 17 or later
- Maven (or use the included `./mvnw`)
- Docker and Docker Compose
- PostgreSQL (or Supabase project)

### Clone the repository
```bash
git clone https://github.com/medhxnsh/Smart_Curriculum_Activity_And_Attendance.git
cd Smart_Curriculum_Activity_And_Attendance
```

---

## Environment Variables (.env.example)
Create a `.env` file at the project root. The following is a minimal example; do **not** commit real secrets.

```env
# Database connection (postgres)
DATABASE_URL=postgres://username:password@localhost:5432/smart_curriculum

# Spring Boot datasource (if using Spring)
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/smart_curriculum
SPRING_DATASOURCE_USERNAME=username
SPRING_DATASOURCE_PASSWORD=password

# JWT secret for authentication
JWT_SECRET=replace_with_secure_secret

# Application port
SERVER_PORT=8080
```

Save as `.env.example` and when configuring locally copy to `.env` and replace placeholders:
```bash
cp .env.example .env
```

---

## Run with Docker Compose

Start services:
```bash
docker-compose up -d
```

Stop and remove containers:
```bash
docker-compose down
```

Log output:
```bash
docker-compose logs -f
```

---

## Database Schema & Migrations
Migrations are stored under `supabase/migrations`. They include table creation and essential constraints for:

Core tables:
- `institutions` — stores institution metadata
- `users` — user records (with role references)
- `roles` — role definitions (ADMIN, TEACHER, STUDENT)
- `classes` / `sections` — grouping of students
- `subjects` — subject definitions
- `attendance` — attendance logs (student_id, class_id, subject_id, timestamp, method, status)
- `curriculum_activities` — activity definitions and metadata
- `activity_assignments` — mapping activities to classes/students and status
- `audit_logs` — optional audit trail for changes

Apply migrations (example):
```bash
psql -h localhost -U <db_user> -d smart_curriculum -f supabase/migrations/0001_initial.sql
# run following migration files in order if present
```

If using Supabase CLI:
```bash
supabase db push
```

---

## Mermaid ER Diagram
Include this in README to visually represent core entities. GitHub renders Mermaid in Markdown if enabled; otherwise use Mermaid live editor or include a PNG generated from the diagram.

```mermaid
erDiagram
    INSTITUTIONS {
        uuid institution_id PK
        string name
        string address
        timestamp created_at
    }
    ROLES {
        uuid role_id PK
        string name
    }
    USERS {
        uuid user_id PK
        string email
        string full_name
        uuid institution_id FK
        uuid role_id FK
        boolean active
        timestamp created_at
    }
    CLASSES {
        uuid class_id PK
        string name
        uuid institution_id FK
    }
    SUBJECTS {
        uuid subject_id PK
        string name
        uuid class_id FK
    }
    ATTENDANCE {
        uuid attendance_id PK
        uuid student_id FK "-> USERS.user_id"
        uuid class_id FK
        uuid subject_id FK
        timestamp recorded_at
        string status
        string method
        uuid recorded_by FK "-> USERS.user_id"
    }
    CURRICULUM_ACTIVITIES {
        uuid activity_id PK
        string title
        text description
        date due_date
        uuid created_by FK "-> USERS.user_id"
    }
    ACTIVITY_ASSIGNMENTS {
        uuid assignment_id PK
        uuid activity_id FK
        uuid class_id FK
        uuid student_id FK
        string status
        timestamp assigned_at
    }
    AUDIT_LOGS {
        uuid log_id PK
        uuid user_id FK
        string action
        text metadata
        timestamp created_at
    }

    INSTITUTIONS ||--o{ USERS : has
    ROLES ||--o{ USERS : defines
    INSTITUTIONS ||--o{ CLASSES : owns
    CLASSES ||--o{ SUBJECTS : contains
    CLASSES ||--o{ USERS : "students_in"
    USERS ||--o{ ATTENDANCE : records
    SUBJECTS ||--o{ ATTENDANCE : "for_subject"
    USERS ||--o{ CURRICULUM_ACTIVITIES : creates
    CURRICULUM_ACTIVITIES ||--o{ ACTIVITY_ASSIGNMENTS : assigned_to
    USERS ||--o{ AUDIT_LOGS : "performs"
```

---

## Workflows (Mermaid Flowcharts)

### Attendance Flow
```mermaid
flowchart TD
  A[Teacher / QR Scanner] --> B{Is QR valid?}
  B -->|Yes| C[Lookup Student & Class]
  B -->|No| D[Manual Entry]
  C --> E[Record attendance entry]
  D --> E
  E --> F{Duplicate?}
  F -->|Yes| G[Resolve duplicate (alert/log)]
  F -->|No| H[Persist attendance, notify student]
  H --> I[Update attendance reports]
```

### Curriculum / Activity Flow
```mermaid
flowchart LR
  TA[Teacher creates activity] --> AB{Target audience}
  AB -->|Class| AC[Assign to class]
  AB -->|Students| AD[Assign to specific students]
  AC --> AE[Notify students]
  AD --> AE
  AE --> AF[Students submit / mark completed]
  AF --> AG[Teacher reviews & grades]
  AG --> AH[Update student activity status & reports]
```

### User Onboarding Flow
```mermaid
flowchart TD
  U[Admin creates user] --> E1[Send invite email]
  E1 --> E2[User registers & sets password]
  E2 --> E3[Assign role & link to institution/class]
  E3 --> E4[User can login]
  E4 --> E5{First time login?}
  E5 -->|Yes| E6[Redirect to onboarding / profile setup]
  E5 -->|No| E7[Redirect to dashboard]
```

---

## API Endpoints (Examples)
> Replace `<base-url>` with your server address (default `http://localhost:8080`).

### Authentication
```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "teacher@example.com",
  "password": "secure_password",
  "fullName": "John Doe",
  "role": "TEACHER",
  "institutionId": "<uuid>"
}
```

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "teacher@example.com",
  "password": "secure_password"
}
```

Response includes `accessToken` (JWT) to use in `Authorization: Bearer <token>` header.

### Attendance
```http
POST /api/attendance/mark
Authorization: Bearer <token>
Content-Type: application/json

{
  "studentId": "<uuid>",
  "classId": "<uuid>",
  "subjectId": "<uuid>",
  "status": "PRESENT",
  "method": "QR"
}
```

```http
GET /api/attendance/class/{classId}?date=2025-09-20
Authorization: Bearer <token>
```

### Curriculum
```http
POST /api/curriculum/create
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Chapter 5 Homework",
  "description": "Exercises 1-10",
  "dueDate": "2025-10-01",
  "target": {
    "type": "CLASS",
    "id": "<classId>"
  }
}
```

---

## Build, Run & Test

### Build
```bash
./mvnw clean package
```

### Run
```bash
java -jar target/<artifact-name>.jar
```

### Run in development
```bash
./mvnw spring-boot:run
```

### Tests
```bash
./mvnw test
```

---

## Architecture
High-level architecture:
```
[Frontend App] <---> [Java Spring Boot API] <---> [PostgreSQL / Supabase]
                       |                          ^
                       |                          |
                       +--> [Auth (JWT)]          +--> [Migrations]
                       +--> [Audit & Logging]
```

- The backend exposes REST endpoints and enforces role-based authorization.
- Database migration files maintain schema and make deployments reproducible.
- Docker Compose provides quick local environment bootstrapping.

---

## Future Enhancements
- Dedicated frontend (React / Angular / Flutter) with role-specific dashboards
- Mobile apps for Android / iOS
- Analytics and visual dashboards (attendance trends, activity completion)
- Notifications (email, SMS, push)
- LMS integrations and grade sync
- Biometric/device integration for attendance
- Multi-tenant SaaS support and billing

---

## Contributing
1. Fork the repository  
2. Create a feature branch (`git checkout -b feature/my-feature`)  
3. Commit changes with clear messages (`git commit -m "Add feature"`)  
4. Push to your branch (`git push origin feature/my-feature`)  
5. Open a Pull Request and describe the change and rationale

Before submitting:
- Run `./mvnw test`
- Ensure database migrations are added under `supabase/migrations`
- Update README and API docs if you add endpoints

---

## License
This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.

---

## Author
Medhansh Vibhu  
GitHub: https://github.com/medhxnsh

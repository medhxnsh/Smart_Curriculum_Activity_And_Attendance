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

### Core Technologies
- Java 17+ (Spring Boot, Maven)
- PostgreSQL / Supabase
- Docker / Docker Compose
- JWT Authentication

### Logos / Badges
![Java](https://img.shields.io/badge/Java-17-blue?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-green?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?logo=postgresql&logoColor=white)
![Supabase](https://img.shields.io/badge/Supabase-Edge-green?logo=supabase&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-blue?logo=docker&logoColor=white)

---

## Getting Started

### Prerequisites
- Java 17+
- Maven (or use `./mvnw`)
- Docker and Docker Compose
- PostgreSQL (or a Supabase project)

### Clone the Repository
```bash
git clone https://github.com/medhxnsh/Smart_Curriculum_Activity_And_Attendance.git
cd Smart_Curriculum_Activity_And_Attendance
```

---

## Environment Variables (.env.example)
```env
# Database connection (Postgres)
DATABASE_URL=postgres://username:password@localhost:5432/smart_curriculum

# Spring Boot datasource
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/smart_curriculum
SPRING_DATASOURCE_USERNAME=username
SPRING_DATASOURCE_PASSWORD=password

# JWT secret for authentication
JWT_SECRET=replace_with_secure_secret

# Application port
SERVER_PORT=8080
```

---

## Run with Docker Compose
```bash
docker-compose up -d
```

Stop and remove containers:
```bash
docker-compose down
```

View logs:
```bash
docker-compose logs -f
```

---

## Database Schema & Migrations
Apply migrations manually:
```bash
psql -h localhost -U <db_user> -d smart_curriculum -f supabase/migrations/0001_initial.sql
```

Or with Supabase CLI:
```bash
supabase db push
```

---

## Mermaid ER Diagram
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
        uuid student_id FK
        uuid class_id FK
        uuid subject_id FK
        timestamp recorded_at
        string status
        string method
        uuid recorded_by FK
    }
    CURRICULUM_ACTIVITIES {
        uuid activity_id PK
        string title
        text description
        date due_date
        uuid created_by FK
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
    CLASSES ||--o{ USERS : students_in
    USERS ||--o{ ATTENDANCE : records
    SUBJECTS ||--o{ ATTENDANCE : for_subject
    USERS ||--o{ CURRICULUM_ACTIVITIES : creates
    CURRICULUM_ACTIVITIES ||--o{ ACTIVITY_ASSIGNMENTS : assigned_to
    USERS ||--o{ AUDIT_LOGS : performs
```

---

## Workflows (Mermaid)

### Attendance Flow
```mermaid
flowchart TD
  A[Teacher or QR Scanner] --> B{Is QR valid}
  B -->|Yes| C[Lookup student and class]
  B -->|No| D[Manual entry by teacher]
  C --> E[Record attendance entry]
  D --> E
  E --> F{Is duplicate}
  F -->|Yes| G[Resolve duplicate and log]
  F -->|No| H[Persist attendance and notify student]
  H --> I[Update attendance reports]
```

### Curriculum / Activity Flow
```mermaid
flowchart LR
  T[Teacher creates activity] --> Chooser{Target audience}
  Chooser -->|Class| AssignClass[Assign to class]
  Chooser -->|Students| AssignStudents[Assign to students]
  AssignClass --> Notify[Notify students]
  AssignStudents --> Notify
  Notify --> Submit[Students submit or mark complete]
  Submit --> Review[Teacher reviews and grades]
  Review --> Update[Update student activity status and reports]
```

### User Onboarding Flow
```mermaid
flowchart TD
  Admin[Admin creates user] --> Invite[Send invitation email]
  Invite --> Register[User registers and sets password]
  Register --> Link[Assign role and link to institution/class]
  Link --> Login[User logs in]
  Login --> FirstTime{First time login}
  FirstTime -->|Yes| Onboard[Redirect to onboarding and profile setup]
  FirstTime -->|No| Dashboard[Redirect to dashboard]
```

---

## API Endpoints (Examples)

### Authentication
```http
POST /api/auth/register
Content-Type: application/json
```
```json
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
```
```json
{
  "email": "teacher@example.com",
  "password": "secure_password"
}
```

### Attendance
```http
POST /api/attendance/mark
Authorization: Bearer <token>
Content-Type: application/json
```
```json
{
  "studentId": "<uuid>",
  "classId": "<uuid>",
  "subjectId": "<uuid>",
  "status": "PRESENT",
  "method": "QR"
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

### Development
```bash
./mvnw spring-boot:run
```

### Tests
```bash
./mvnw test
```

---

## Architecture
```
[Frontend App] <---> [Java Spring Boot API] <---> [PostgreSQL / Supabase]
                       |                          ^
                       |                          |
                       +--> [Auth (JWT)]          +--> [Migrations]
                       +--> [Audit & Logging]
```

---

## Future Enhancements
- React/Angular/Flutter frontend with dashboards
- Mobile apps for teachers and students
- Analytics dashboards for attendance & performance
- Notifications (email, SMS, push)
- Biometric attendance integration
- Multi-tenant SaaS features

---

## Contributing
1. Fork the repository  
2. Create a feature branch (`git checkout -b feature/my-feature`)  
3. Commit changes (`git commit -m "Add feature"`)  
4. Push (`git push origin feature/my-feature`)  
5. Open a Pull Request  

---

## License
This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.

---

## Author
**Medhansh Vibhu**  
GitHub: [medhxnsh](https://github.com/medhxnsh)

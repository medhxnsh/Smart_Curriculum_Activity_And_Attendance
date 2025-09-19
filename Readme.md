# Smart Curriculum Activity And Attendance

Smart Curriculum Activity And Attendance is a **full-stack platform** for managing attendance and curriculum activities in educational institutions.  
It simplifies **attendance tracking**, supports **curriculum planning**, and provides **role-based access** for administrators, teachers, and students.  
The project is designed with scalability, modularity, and ease of deployment in mind.

---

## 📖 Overview
Educational institutions often struggle with manual attendance and scattered activity management.  
This project provides a **digital-first solution** with:
- Role-based authentication
- Centralized attendance tracking
- Curriculum and activity management
- Integration with PostgreSQL (or Supabase)
- Dockerized deployment for easy setup

---

## ✨ Features
- **Role Management**
  - Admin: Manage users, institutions, and activities
  - Teacher: Record and monitor attendance, assign curriculum
  - Student: View attendance and activities
- **Attendance Tracking**
  - QR code-based and manual entry
  - Historical logs and reports
- **Curriculum Activities**
  - Create, assign, and track activities
- **Database**
  - PostgreSQL with Supabase-compatible migrations
- **Deployment**
  - Docker Compose for easy local development
  - Environment-driven configuration
- **Backend**
  - Java (Spring Boot + Maven)
  - RESTful APIs with modular structure

---

## 📂 Repository Structure
```
.
├── src/                        # Java backend source code
├── supabase/
│   └── migrations/             # SQL migrations for PostgreSQL/Supabase
├── docker-compose.yml          # Docker Compose file for local setup
├── pom.xml                     # Maven build configuration
├── mvnw, mvnw.cmd              # Maven wrapper scripts
├── LICENSE                     # MIT License
└── README.md                   # Project documentation
```

---

## 🛠️ Technology Stack

### Backend
- ![Java](https://img.shields.io/badge/Java-17-blue?logo=openjdk&logoColor=white)  
- ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-green?logo=springboot&logoColor=white)  
- ![Maven](https://img.shields.io/badge/Maven-Build-orange?logo=apachemaven&logoColor=white)

### Database
- ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?logo=postgresql&logoColor=white)  
- ![Supabase](https://img.shields.io/badge/Supabase-Edge-green?logo=supabase&logoColor=white)

### Deployment
- ![Docker](https://img.shields.io/badge/Docker-Compose-blue?logo=docker&logoColor=white)

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven (or use `./mvnw`)
- Docker and Docker Compose
- PostgreSQL (local or Supabase)

### Clone the Repository
```bash
git clone https://github.com/medhxnsh/Smart_Curriculum_Activity_And_Attendance.git
cd Smart_Curriculum_Activity_And_Attendance
```

### Environment Setup
Create a `.env` file in the project root:
```env
# Database connection
DATABASE_URL=postgres://user:password@localhost:5432/smart_curriculum

# Spring Boot datasource
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/smart_curriculum
SPRING_DATASOURCE_USERNAME=your_user
SPRING_DATASOURCE_PASSWORD=your_password

# JWT secret for authentication
JWT_SECRET=your_secret

# Optional: Application port
SERVER_PORT=8080
```

---

## 🐳 Running with Docker Compose
Start containers:
```bash
docker-compose up -d
```

Stop and remove containers:
```bash
docker-compose down
```

---

## 🗄️ Database Migrations
Migrations are located under `supabase/migrations`.

Apply them manually:
```bash
psql -h localhost -U your_user -d smart_curriculum -f supabase/migrations/0001_initial.sql
```

Or apply via Supabase CLI:
```bash
supabase db push
```

---

## ⚙️ Build and Run Backend

### Build
```bash
./mvnw clean package
```

### Run
```bash
java -jar target/<artifact-name>.jar
```

Or run directly in development:
```bash
./mvnw spring-boot:run
```

---

## 📡 API Endpoints
Some example modules:

- Authentication → `/api/auth/*`
- Attendance → `/api/attendance/*`
- Curriculum → `/api/curriculum/*`
- Administration → `/api/admin/*`

You can extend this with **Swagger/OpenAPI** for live documentation.

---

## 📐 Architecture
```
+---------------------------+
|        Frontend App       |  <- (planned, connects to REST APIs)
+---------------------------+
            |
            v
+---------------------------+
|     Java Spring Boot      |  <- REST APIs & Business Logic
+---------------------------+
            |
            v
+---------------------------+
|   PostgreSQL / Supabase   |  <- Database Layer with migrations
+---------------------------+
```

---

## 🧪 Testing
Run unit and integration tests:
```bash
./mvnw test
```

---

## 🤝 Contributing
1. Fork the repository  
2. Create a feature branch (`git checkout -b feature/my-feature`)  
3. Commit changes (`git commit -m "Add feature"`)  
4. Push to your branch (`git push origin feature/my-feature`)  
5. Open a Pull Request  

---

## 📜 License
This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.

---

## 👤 Author
**Medhansh Vibhu**  
GitHub: [medhxnsh](https://github.com/medhxnsh)

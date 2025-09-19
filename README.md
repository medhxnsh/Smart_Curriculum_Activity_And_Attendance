Smart Curriculum Activity And Attendance

A smart system for managing curriculum activities and attendance tracking.

Features

Activity Management: Create, update, and track curriculum activities
Attendance Tracking: Automated attendance monitoring system
Smart Analytics: Data-driven insights on participation and engagement
User Management: Role-based access control for different user types
Reporting: Generate comprehensive reports on activities and attendance
Installation

Clone the repository:
bash
git clone https://github.com/medhxnsh/Smart_Curriculum_Activity_And_Attendance.git
cd Smart_Curriculum_Activity_And_Attendance
Install dependencies:
bash
npm install
Set up environment variables:
bash
cp .env.example .env
# Edit .env with your configuration
Initialize the database:
bash
npm run db:migrate
Start the application:
bash
npm start
Usage

After installation, access the application through your web browser at http://localhost:3000 (or your configured port).

Admin Features

Manage users and permissions
Create and schedule activities
Generate attendance reports
Monitor system analytics
User Features

View scheduled activities
Mark attendance (if applicable)
Track personal participation history
Configuration

The application can be configured through environment variables:

PORT: Server port (default: 3000)
DB_HOST: Database host
DB_NAME: Database name
DB_USER: Database user
DB_PASS: Database password
JWT_SECRET: Secret key for JWT tokens
API Documentation

API endpoints are available at /api path. Detailed API documentation can be found in the docs/ directory.

Development

To contribute to this project:

Fork the repository
Create a feature branch: git checkout -b feature-name
Make your changes and commit: git commit -m 'Add feature'
Push to the branch: git push origin feature-name
Submit a pull request
License

This project is licensed under the MIT License - see the LICENSE file for details.

Support

For support, email support@example.com or create an issue in the GitHub repository.

Contributing

We welcome contributions! Please read our contributing guidelines in CONTRIBUTING.md before submitting pull requests.

Changelog

See CHANGELOG.md for version history and changes.

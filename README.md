Welcome to the Smart Contact Manager repository! This application is designed to manage contacts efficiently with distinct functionalities for Admin and Normal users. Below is the comprehensive guide on setting up and using the Smart Contact Manager.

Features
Admin Side
Dynamic dashboard
Manage user accounts
View all contacts
Add, update, and delete contacts
Upload and manage photos
User Side
Dynamic dashboard
Add, update, and delete personal contacts
Upload and manage photos for contacts
Technology Stack
Backend: Spring Boot
Frontend: HTML, CSS, JavaScript
Getting Started
Prerequisites
Ensure you have the following installed:

Java JDK 11 or higher
Maven
Node.js (for frontend dependency management, if required)
A web browser (for accessing the application)
Installation
Clone the repository:

bash
Copy code
git clone https://github.com/yourusername/smart-contact-manager.git
cd smart-contact-manager
Backend Setup:

Navigate to the backend directory (if applicable).
Build the project using Maven:
bash
Copy code
mvn clean install
Run the Spring Boot application:
bash
Copy code
mvn spring-boot:run
Frontend Setup:

Ensure all frontend dependencies are resolved.
Open index.html in your web browser or set up a local server to serve static files.
Configuration
Database Configuration:
Update the application.properties file located in src/main/resources with your database configurations:
properties
Copy code
spring.datasource.url=jdbc:mysql://localhost:3306/yourdatabase
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
Usage
Admin Dashboard
Login: Admin users can log in via the /admin/login page.
Manage Users: Admins can add, update, or delete user accounts.
Manage Contacts: View and manage all contacts across the system.
Photo Management: Upload, update, or delete contact photos.
User Dashboard
Login: Normal users can log in via the /user/login page.
Manage Contacts: Add, update, or delete personal contacts.
Photo Management: Upload, update, or delete photos for personal contacts.
Contributing
We welcome contributions! Please follow these steps:

Fork the repository.
Create your feature branch (git checkout -b feature/YourFeature).
Commit your changes (git commit -m 'Add some feature').
Push to the branch (git push origin feature/YourFeature).
Open a pull request.

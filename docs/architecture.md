# Employee Management System (EMS) - Architecture

## Overview
The Employee Management System is a simple, lightweight Java web application designed for a college project. It allows administrators to manage employee records and allows employees to view their profiles.

## Tech Stack
* **Language**: Java 8
* **Backend**: Java Servlets (javax.servlet), JSP, JSTL
* **Database**: SQLite (Zero-config for local testing) / MySQL 8 (For production/college lab)
* **Build Tool**: Maven
* **Server**: Apache Tomcat 9

## Application Architecture (MVC)
The application strictly follows the Model-View-Controller (MVC) design pattern.

### 1. Model Layer (`com.ems.model`)
Contains plain Java objects (POJOs) that represent the database entities.
* `User.java`: Represents authentication credentials and roles.
* `Employee.java`: Represents detailed employee information.

### 2. Data Access Object (DAO) Layer (`com.ems.dao`)
Handles all direct interactions with the database using JDBC `PreparedStatement`.
* `UserDAO.java`: Fetches user credentials for login.
* `EmployeeDAO.java`: Handles CRUD (Create, Read, Update, Delete) operations for employees, including pagination logic.

### 3. Controller Layer (`com.ems.servlet`)
Receives HTTP requests, processes business logic, and routes to the appropriate view.
* `LoginServlet.java`: Handles authentication and session creation.
* `LogoutServlet.java`: Destroys the session.
* `AdminServlet.java`: Maps to `/admin/dashboard`. Manages the employee list and CRUD actions.
* `EmployeeServlet.java`: Maps to `/employee/profile`. Shows the logged-in user their details.

### 4. View Layer (`src/main/webapp`)
JSP files handle the presentation logic using JSTL for conditional rendering and loops.
* Uses a custom "white aesthetic" CSS design (`style.css`) with Syne and Manrope fonts.
* Avoids generic UI frameworks, ensuring a unique and clean presentation.

## Database Schema
The system uses a highly normalized relational database with two primary tables.

### `users` table
Stores authentication information.
* `id`: Primary Key
* `username`: Unique username (usually derived from email)
* `password`: SHA-256 Hashed password
* `role`: Enum ('ADMIN', 'EMPLOYEE')

### `employees` table
Stores the actual employee profiles.
* `id`: Primary Key
* `user_id`: Foreign Key linking to `users.id`
* `name`, `department`, `salary`, `email`, `phone`: Core data fields
* `created_at`, `updated_at`: Audit timestamps

## Security & Utilities
* **AuthFilter**: A Servlet Filter that intercepts requests to `/admin/*` and `/employee/*` ensuring the user has an active session and the correct role.
* **PasswordUtil**: Hashes passwords using SHA-256 before database insertion.
* **DBConnection**: A singleton utility that establishes the JDBC connection, automatically detecting and switching between SQLite and MySQL based on configuration.

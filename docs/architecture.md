# Employee Management System - Architecture

## Overview

This is a Java web application that runs on Apache Tomcat. It uses the Model-View-Controller pattern to separate concerns cleanly across layers.


## Technology Choices

- **Java 8** as the language, using the javax.servlet namespace (required for Tomcat 9).
- **JSP with JSTL** for server-side HTML rendering. No frontend framework.
- **JDBC with PreparedStatement** for all database access. No ORM.
- **Maven** for dependency management and WAR packaging.
- **SQLite** for zero-config local development, with a MySQL option for production.
- **Vanilla CSS** with Google Fonts (Syne and Manrope) for the UI.


## Architecture Layers

### 1. Model Layer (com.ems.model)

Plain Java objects that represent database rows.

- `User.java` has four fields: id, username, password (SHA-256 hash), and role.
- `Employee.java` has ten fields matching the employees table columns.
- `Task.java` has six fields matching the employee_tasks table columns.

These classes have no business logic. They are data containers with getters and setters.

### 2. Data Access Layer (com.ems.dao)

Each DAO class talks to the database using JDBC.

- `UserDAO` has one method: `getUserByUsername`. It is called during login.
- `EmployeeDAO` handles the full employee lifecycle: list with pagination, get by ID, get by user ID, add (with transactional user creation), update, delete (with transactional user deletion), get all names, and get aggregate statistics.
- `TaskDAO` handles task CRUD: list all, list by employee name, get by ID, add, update status, and delete.

All DAO methods use try-with-resources for connection management and throw RuntimeException on failure instead of silently swallowing errors.

### 3. Controller Layer (com.ems.servlet)

Servlets receive HTTP requests, call the DAO layer, and forward results to JSP views.

- `LoginServlet` handles GET (show login form) and POST (authenticate and create session).
- `LogoutServlet` destroys the session.
- `AdminServlet` is the main admin controller. It uses a single URL (/admin/dashboard) with an "action" query parameter to route between dashboard view, employee CRUD, and task operations.
- `EmployeeServlet` handles employee profile viewing and task viewing, also routed by an "action" parameter.

### 4. Filter Layer (com.ems.filter)

`AuthFilter` is mapped to `/admin/*` and `/employee/*` in web.xml. It checks for an active session and verifies the user's role matches the URL path they are trying to access. Unauthenticated users are redirected to the login page.

### 5. Utility Layer (com.ems.util)

- `DBConnection` is the database connection factory. It has a boolean toggle to switch between SQLite and MySQL. When using SQLite, it auto-creates tables and seeds sample data on first startup.
- `PasswordUtil` converts plain text passwords to SHA-256 hex strings.
- `EmailUtil` sends email via Gmail SMTP. It has an enable/disable toggle so the app works without email configuration.

### 6. View Layer (src/main/webapp)

JSP files render HTML using JSTL tags for loops, conditionals, and formatting. All pages share a common CSS file (style.css) that implements a monochromatic white and black design system.


## Database Schema

Three tables:

- `users` stores login credentials and role (ADMIN or EMPLOYEE).
- `employees` stores profile data and has a foreign key to users.
- `employee_tasks` stores task assignments. Tasks reference employees by name.


## Request Flow

```
Browser Request
    |
    v
AuthFilter (checks session and role)
    |
    v
Servlet (reads parameters, calls DAO)
    |
    v
DAO (executes SQL via PreparedStatement)
    |
    v
Servlet (sets request attributes)
    |
    v
JSP (renders HTML response)
```


## Security

- Passwords are hashed with SHA-256 before storage. Plain text passwords are never stored.
- All SQL queries use PreparedStatement to prevent injection.
- Session timeout is set to 30 minutes in web.xml.
- AuthFilter blocks direct URL access to protected pages.

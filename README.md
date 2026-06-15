# Employee Management System

A web-based Employee Management System built with Java Servlets, JSP, JDBC, and MySQL as a college assignment. The application runs on Apache Tomcat and follows the Model-View-Controller (MVC) architecture.


## What This Project Does

This system has two types of users: administrators and employees.

**Admins can:**
- View a dashboard with employee statistics (total headcount, average salary, monthly salary bill)
- Add, edit, and delete employee records with full form validation
- Assign tasks to employees with deadlines
- Manage task statuses (pending, in progress, completed)
- Delete tasks

**Employees can:**
- Log in with their credentials and view their personal profile
- See tasks assigned to them
- Update their own task status as they work on it

When an admin adds a new employee or assigns a task, the system sends an email notification to the employee automatically. If email is not configured, the notification is silently skipped and logged to the console instead.


## Tech Stack

| Layer      | Technology                           |
|------------|--------------------------------------|
| Language   | Java 8                               |
| Backend    | Java Servlets (javax.servlet), JSP   |
| Database   | SQLite (local) / MySQL 8 (lab)       |
| Build      | Maven                                |
| Server     | Apache Tomcat 9                      |
| Email      | JavaMail API with Gmail SMTP         |
| Frontend   | JSP, JSTL, vanilla CSS               |


## Project Structure

```
EmployeeManagementSystem/
  pom.xml
  schema.sql
  docs/
    architecture.md
  src/
    main/
      java/com/ems/
        model/
          User.java            -- User POJO (id, username, password, role)
          Employee.java        -- Employee POJO (name, dept, salary, email, phone, address, state)
          Task.java            -- Task POJO (title, description, deadline, status)
        dao/
          UserDAO.java         -- Fetches user by username for login
          EmployeeDAO.java     -- CRUD for employees, stats queries, employee name list
          TaskDAO.java         -- CRUD for tasks, fetch by employee name
        servlet/
          LoginServlet.java    -- Handles login form and session creation
          LogoutServlet.java   -- Destroys session and redirects to login
          AdminServlet.java    -- All admin actions (employee CRUD, task CRUD, dashboard)
          EmployeeServlet.java -- Employee profile view and task view
        filter/
          AuthFilter.java      -- Blocks unauthenticated access to /admin/* and /employee/*
        util/
          DBConnection.java    -- Database connection factory (SQLite or MySQL)
          PasswordUtil.java    -- SHA-256 password hashing
          EmailUtil.java       -- Gmail SMTP email sender
      webapp/
        login.jsp
        css/style.css
        WEB-INF/web.xml
        admin/
          dashboard.jsp        -- Employee table with pagination, sorting, stats cards
          add-employee.jsp     -- Form for registering a new employee
          edit-employee.jsp    -- Form for editing an existing employee
          tasks.jsp            -- Table of all assigned tasks
          assign-task.jsp      -- Form for assigning a task to an employee
        employee/
          profile.jsp          -- Employee profile details
          tasks.jsp            -- Employee's assigned tasks with status update
    test/
      java/com/ems/dao/
        UserDAOTest.java
        EmployeeDAOTest.java
```


## How the Code Works

### Authentication Flow

1. The user opens the app and lands on `login.jsp`.
2. They submit their username and password.
3. `LoginServlet` hashes the password using SHA-256 and compares it against the stored hash in the database via `UserDAO`.
4. If the credentials match, a session is created with the user's ID and role. The user is redirected to either the admin dashboard or the employee profile depending on their role.
5. `AuthFilter` runs on every request to `/admin/*` and `/employee/*`. If there is no active session, the user is sent back to the login page. If they are logged in but trying to access pages meant for the other role, they are redirected to their own section.

### Admin Operations

All admin actions go through `AdminServlet`, which is mapped to `/admin/dashboard`. The servlet reads an `action` query parameter to decide what to do:

- No action or `dashboard`: fetches the employee list (with pagination and sorting) and stats, forwards to `dashboard.jsp`.
- `add`: shows the add form on GET, validates and inserts on POST.
- `edit`: loads the employee into the form on GET, validates and updates on POST.
- `delete`: deletes the employee and their linked user account in a single transaction.
- `tasks`: shows all assigned tasks.
- `assign`: shows the assign-task form on GET, inserts the task on POST and sends an email.
- `deleteTask`: deletes a task.
- `updateTaskStatus`: changes a task's status.

### Employee Operations

`EmployeeServlet` is mapped to `/employee/profile`. It looks up the employee record using the `userId` stored in the session.

- No action: shows the profile page.
- `action=tasks`: fetches tasks by the employee's name and shows them.
- `action=updateTaskStatus` (POST): lets the employee change their own task status.

### Database

The app supports two databases:
- **SQLite** for local development. When the app starts, `DBConnection` checks if the tables exist. If they do not, it creates them and inserts sample data automatically. No setup needed.
- **MySQL** for the college lab. You flip a boolean in `DBConnection.java`, set your MySQL credentials, and run `schema.sql` manually.

All database queries use `PreparedStatement` to prevent SQL injection.

### Email

`EmailUtil` has a boolean flag `EMAIL_ENABLED`. When set to false (the default), it just prints a log message saying what email it would have sent. When set to true with valid Gmail SMTP credentials, it sends real emails. This way the app works perfectly even without email configuration.


## Database Tables

**users** - stores login credentials

| Column   | Type         | Notes                  |
|----------|--------------|------------------------|
| id       | INT (PK)     | Auto increment         |
| username | VARCHAR(50)  | Unique                 |
| password | VARCHAR(255) | SHA-256 hash           |
| role     | ENUM         | ADMIN or EMPLOYEE      |

**employees** - stores employee profiles

| Column     | Type          | Notes                     |
|------------|---------------|---------------------------|
| id         | INT (PK)      | Auto increment            |
| user_id    | INT (FK)      | References users.id       |
| name       | VARCHAR(100)  |                           |
| department | VARCHAR(100)  |                           |
| salary     | DECIMAL(10,2) |                           |
| email      | VARCHAR(150)  | Unique                    |
| phone      | VARCHAR(20)   |                           |
| address    | VARCHAR(255)  | Optional                  |
| state      | VARCHAR(100)  | Optional                  |
| created_at | TIMESTAMP     | Auto-set on creation      |
| updated_at | TIMESTAMP     | Auto-updated on change    |

**employee_tasks** - stores assigned tasks

| Column           | Type         | Notes                              |
|------------------|--------------|------------------------------------|
| task_id          | INT (PK)     | Auto increment                     |
| employee_name    | VARCHAR(100) | Name of the assigned employee      |
| task_title       | VARCHAR(200) |                                    |
| task_description | TEXT         | Optional                           |
| deadline         | VARCHAR(20)  | Date string (yyyy-MM-dd)           |
| status           | ENUM         | PENDING, IN_PROGRESS, or COMPLETED |


## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Apache Maven
- Apache Tomcat 9

### Build

Open a terminal in the project root (where `pom.xml` is located) and run:

```
mvn clean package
```

This downloads all dependencies and produces `target/EmployeeManagementSystem.war`.

### Database Setup

**For local testing (default):** No setup needed. The app uses an embedded SQLite database and seeds sample data on first run.

**For MySQL (college lab):**
1. Open `src/main/java/com/ems/util/DBConnection.java`.
2. Change `USE_SQLITE` to `false`.
3. Set your MySQL username and password in the same file.
4. Run `schema.sql` against your MySQL server.

### Deploy

1. Copy `target/EmployeeManagementSystem.war` into Tomcat's `webapps` directory.
2. Start Tomcat.
3. Open `http://localhost:8080/EmployeeManagementSystem/login` in your browser.

### Default Login Credentials

**Admin:**
- Username: admin
- Password: admin123

**Employee:**
- Username: vihaan.patel
- Password: employee123


## Running Tests

```
mvn test
```

This runs the JUnit 5 tests in `src/test/java` which verify that the DAO layer correctly reads and writes to the database.


## Email Configuration (Optional)

To enable real email notifications:

1. Open `src/main/java/com/ems/util/EmailUtil.java`.
2. Set `EMAIL_ENABLED` to `true`.
3. Enter your Gmail address and an App Password (not your regular Gmail password).
4. To generate an App Password: go to your Google Account, enable 2-Step Verification, then create an App Password under Security settings.


## Key Design Decisions

- **Single AdminServlet for all admin actions.** Instead of creating separate servlets for employees and tasks, a single servlet uses an `action` parameter. This keeps the number of servlet mappings small and makes the routing easy to follow.
- **SQLite as the default database.** The project auto-initializes and seeds data on first run. This means anyone can clone the repo and run it immediately without installing MySQL or running SQL scripts.
- **Email fails silently.** If the SMTP connection fails, the error is logged but the main operation (adding an employee, assigning a task) still succeeds. The user is never shown an email error.
- **Tasks linked by employee name, not ID.** This is simpler and avoids JOINs. For a project of this scale, it is the right trade-off.

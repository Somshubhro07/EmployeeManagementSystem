# Employee Management System

A web-based Employee Management System built with Java Servlets, JSP, and Maven. This project is designed as an advanced college assignment demonstrating the Model-View-Controller architecture, secure authentication, database management, and a custom minimalist frontend design.

## Features

* **Admin Dashboard**: Create, read, update, and delete employee records with pagination and sorting capabilities.
* **Employee Portal**: Employees can log in to view their personal profile and salary details.
* **Role-Based Access Control**: Secure routes protected by Servlet Filters ensuring employees cannot access admin pages.
* **Zero-Config Database**: Uses an embedded SQLite database by default for instant local testing, with the ability to switch to MySQL for production.
* **Custom UI Design**: A striking, bespoke "white aesthetic" frontend built entirely with vanilla CSS (no Bootstrap or generic templates).

## Prerequisites

* Java Development Kit (JDK) 8 or higher
* Apache Tomcat 9
* Apache Maven

## Getting Started

### 1. Build the Project

Open your terminal or command prompt in the project root directory (where the `pom.xml` file is located) and run the Maven package command.

```
mvn clean package
```

This will download all necessary dependencies and generate a WAR file located at `target/EmployeeManagementSystem.war`.

### 2. Database Configuration

By default, the application is configured to use a local SQLite database (`ems.db`). The tables and sample data will be automatically generated the first time you run the application.

If you wish to switch to MySQL for your college presentation:
1. Open `src/main/java/com/ems/util/DBConnection.java`.
2. Change `private static final boolean USE_SQLITE = true;` to `false`.
3. Update the `MYSQL_USER` and `MYSQL_PASSWORD` variables to match your local MySQL setup.
4. Run the provided `schema.sql` file in your MySQL environment to create the tables.

### 3. Deployment

1. Copy the generated `EmployeeManagementSystem.war` file from the `target` directory.
2. Paste it into your Tomcat `webapps` directory.
3. Start the Tomcat server.
4. Open your web browser and navigate to: `http://localhost:8080/EmployeeManagementSystem/login`

### 4. Default Credentials

The database is pre-seeded with the following accounts for testing:

**Administrator Account**
* Username: `admin`
* Password: `admin123`

**Employee Account**
* Username: `vihaan.patel`
* Password: `employee123`

## Testing

The project includes JUnit 5 tests for the Data Access Object (DAO) layer to verify database interactions. To run the tests, execute:

```
mvn test
```

## Project Structure

* `src/main/java/`: Contains all Java source code organized by MVC layers (model, dao, servlet, util, filter).
* `src/main/webapp/`: Contains JSP views, CSS styling, and the `web.xml` deployment descriptor.
* `src/test/java/`: Contains unit tests for the application.
* `docs/`: Contains additional documentation regarding the system architecture.

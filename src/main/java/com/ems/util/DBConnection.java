package com.ems.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    // Toggle this switch:
    // Set to true for zero-config local testing with SQLite (no password needed!)
    // Set to false when presenting on the college PC using MySQL Workbench
    private static final boolean USE_SQLITE = true;

    // SQLite Connection Details
    private static final String SQLITE_URL = "jdbc:sqlite:C:/Users/HP/Desktop/Code stuff/Java_clg/EmployeeManagementSystem/ems.db";

    // MySQL Connection Details (for college PC)
    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/ems_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String MYSQL_USER = "root";
    private static final String MYSQL_PASSWORD = "root";

    static {
        try {
            if (USE_SQLITE) {
                Class.forName("org.sqlite.JDBC");
                initializeSQLiteDatabase();
            } else {
                Class.forName("com.mysql.cj.jdbc.Driver");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Database driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (USE_SQLITE) {
            return DriverManager.getConnection(SQLITE_URL);
        } else {
            return DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
        }
    }

    private static void initializeSQLiteDatabase() {
        try (Connection conn = DriverManager.getConnection(SQLITE_URL);
             Statement stmt = conn.createStatement()) {

            ResultSet rs = conn.getMetaData().getTables(null, null, "users", null);
            if (!rs.next()) {
                System.out.println("Initializing local SQLite database...");

                stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "username TEXT UNIQUE NOT NULL," +
                        "password TEXT NOT NULL," +
                        "role TEXT CHECK(role IN ('ADMIN', 'EMPLOYEE')) NOT NULL" +
                        ")");

                stmt.execute("CREATE TABLE IF NOT EXISTS employees (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "user_id INTEGER," +
                        "name TEXT NOT NULL," +
                        "department TEXT," +
                        "salary REAL," +
                        "email TEXT UNIQUE NOT NULL," +
                        "phone TEXT," +
                        "address TEXT," +
                        "state TEXT," +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                        "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                        "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL" +
                        ")");

                stmt.execute("CREATE TABLE IF NOT EXISTS employee_tasks (" +
                        "task_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "employee_name TEXT NOT NULL," +
                        "task_title TEXT NOT NULL," +
                        "task_description TEXT," +
                        "deadline TEXT," +
                        "status TEXT CHECK(status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED')) NOT NULL DEFAULT 'PENDING'" +
                        ")");

                // Seed users
                stmt.execute("INSERT INTO users (username, password, role) VALUES " +
                        "('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'ADMIN')," +
                        "('vihaan.patel', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE')," +
                        "('aditya.verma', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE')," +
                        "('ananya.iyer', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE')," +
                        "('diya.reddy', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE')," +
                        "('ishaan.gupta', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE')," +
                        "('kabir.malhotra', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE')," +
                        "('meera.joshi', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE')," +
                        "('neha.nair', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE')," +
                        "('rohan.rao', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE')," +
                        "('reyansh.sen', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE')," +
                        "('saanvi.choudhury', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE')," +
                        "('shreya.chatterjee', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE')," +
                        "('vivaan.saxena', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE')," +
                        "('yash.deshmukh', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE')," +
                        "('aisha.kulkarni', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE')");

                // Seed employees
                stmt.execute("INSERT INTO employees (user_id, name, department, salary, email, phone, address, state) VALUES " +
                        "(1,  'Aarav Sharma',       'Management',  95000.00, 'aarav.sharma@company.in',    '9876543210', '12 MG Road',         'Maharashtra')," +
                        "(2,  'Vihaan Patel',       'Engineering', 75000.00, 'vihaan.patel@company.in',    '9876543211', '45 Ring Road',       'Gujarat')," +
                        "(3,  'Aditya Verma',       'Engineering', 82000.00, 'aditya.verma@company.in',    '9876543212', '78 Nehru Nagar',     'Uttar Pradesh')," +
                        "(4,  'Ananya Iyer',        'Marketing',   65000.00, 'ananya.iyer@company.in',     '9876543213', '23 Anna Salai',      'Tamil Nadu')," +
                        "(5,  'Diya Reddy',         'Finance',     78000.00, 'diya.reddy@company.in',      '9876543214', '56 Banjara Hills',   'Telangana')," +
                        "(6,  'Ishaan Gupta',       'Engineering', 88000.00, 'ishaan.gupta@company.in',    '9876543215', '89 Connaught Place', 'Delhi')," +
                        "(7,  'Kabir Malhotra',     'HR',          62000.00, 'kabir.malhotra@company.in',  '9876543216', '34 Sector 17',       'Punjab')," +
                        "(8,  'Meera Joshi',        'Marketing',   70000.00, 'meera.joshi@company.in',     '9876543217', '67 FC Road',         'Maharashtra')," +
                        "(9,  'Neha Nair',          'Finance',     73000.00, 'neha.nair@company.in',       '9876543218', '90 MG Road',         'Kerala')," +
                        "(10, 'Rohan Rao',          'Engineering', 91000.00, 'rohan.rao@company.in',       '9876543219', '12 Koramangala',     'Karnataka')," +
                        "(11, 'Reyansh Sen',        'HR',          60000.00, 'reyansh.sen@company.in',     '9876543220', '45 Park Street',     'West Bengal')," +
                        "(12, 'Saanvi Choudhury',   'Operations',  67000.00, 'saanvi.choudhury@company.in','9876543221', '78 Paltan Bazaar',   'Assam')," +
                        "(13, 'Shreya Chatterjee',  'Engineering', 85000.00, 'shreya.chatterjee@company.in','9876543222','23 Salt Lake',       'West Bengal')," +
                        "(14, 'Vivaan Saxena',      'Operations',  64000.00, 'vivaan.saxena@company.in',   '9876543223', '56 Civil Lines',     'Rajasthan')," +
                        "(15, 'Yash Deshmukh',      'Marketing',   69000.00, 'yash.deshmukh@company.in',   '9876543224', '89 Shivaji Nagar',   'Maharashtra')," +
                        "(16, 'Aisha Kulkarni',     'Finance',     76000.00, 'aisha.kulkarni@company.in',  '9876543225', '34 Deccan Gymkhana', 'Maharashtra')");

                // Seed tasks
                stmt.execute("INSERT INTO employee_tasks (employee_name, task_title, task_description, deadline, status) VALUES " +
                        "('Vihaan Patel',      'API Integration',       'Integrate payment gateway REST API',        '2026-07-01', 'IN_PROGRESS')," +
                        "('Vihaan Patel',      'Unit Testing',          'Write JUnit tests for DAO layer',           '2026-07-10', 'PENDING')," +
                        "('Aditya Verma',      'Database Migration',    'Migrate legacy tables to new schema',       '2026-06-30', 'COMPLETED')," +
                        "('Ananya Iyer',       'Campaign Report',       'Prepare Q2 marketing campaign results',     '2026-07-05', 'PENDING')," +
                        "('Ishaan Gupta',      'Code Review',           'Review pull requests for auth module',       '2026-06-28', 'IN_PROGRESS')," +
                        "('Kabir Malhotra',    'Onboarding Plan',       'Draft onboarding checklist for new hires',   '2026-07-08', 'PENDING')," +
                        "('Rohan Rao',         'Performance Tuning',    'Optimize slow database queries',             '2026-07-12', 'IN_PROGRESS')," +
                        "('Shreya Chatterjee', 'Feature Development',   'Build employee export to CSV feature',       '2026-07-20', 'PENDING')," +
                        "('Meera Joshi',       'Social Media Strategy', 'Plan social media calendar for July',        '2026-06-25', 'COMPLETED')," +
                        "('Diya Reddy',        'Budget Review',         'Review and finalize Q3 department budgets',  '2026-07-15', 'PENDING')");

                System.out.println("SQLite database successfully initialized and seeded!");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize SQLite database", e);
        }
    }
}

-- Employee Management System Database Setup
-- Run this script in MySQL to create the database and seed data.

CREATE DATABASE IF NOT EXISTS ems_db;
USE ems_db;

-- Users table for authentication
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'EMPLOYEE') NOT NULL
);

-- Employees table
CREATE TABLE IF NOT EXISTS employees (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(100),
    salary DECIMAL(10, 2),
    email VARCHAR(150) UNIQUE NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255),
    state VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Employee tasks table
CREATE TABLE IF NOT EXISTS employee_tasks (
    task_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_name VARCHAR(100) NOT NULL,
    task_title VARCHAR(200) NOT NULL,
    task_description TEXT,
    deadline VARCHAR(20),
    status ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED') NOT NULL DEFAULT 'PENDING'
);

-- Seed users
-- Admin password is "admin123" hashed as SHA-256
-- Employee passwords are "employee123" hashed as SHA-256
INSERT INTO users (username, password, role) VALUES
('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'ADMIN'),
('vihaan.patel', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE'),
('aditya.verma', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE'),
('ananya.iyer', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE'),
('diya.reddy', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE'),
('ishaan.gupta', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE'),
('kabir.malhotra', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE'),
('meera.joshi', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE'),
('neha.nair', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE'),
('rohan.rao', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE'),
('reyansh.sen', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE'),
('saanvi.choudhury', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE'),
('shreya.chatterjee', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE'),
('vivaan.saxena', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE'),
('yash.deshmukh', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE'),
('aisha.kulkarni', 'e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f', 'EMPLOYEE');

-- Seed employees
INSERT INTO employees (user_id, name, department, salary, email, phone, address, state) VALUES
(1,  'Aarav Sharma',       'Management',  95000.00, 'aarav.sharma@company.in',    '9876543210', '12 MG Road',          'Maharashtra'),
(2,  'Vihaan Patel',       'Engineering', 75000.00, 'vihaan.patel@company.in',    '9876543211', '45 Ring Road',        'Gujarat'),
(3,  'Aditya Verma',       'Engineering', 82000.00, 'aditya.verma@company.in',    '9876543212', '78 Nehru Nagar',      'Uttar Pradesh'),
(4,  'Ananya Iyer',        'Marketing',   65000.00, 'ananya.iyer@company.in',     '9876543213', '23 Anna Salai',       'Tamil Nadu'),
(5,  'Diya Reddy',         'Finance',     78000.00, 'diya.reddy@company.in',      '9876543214', '56 Banjara Hills',    'Telangana'),
(6,  'Ishaan Gupta',       'Engineering', 88000.00, 'ishaan.gupta@company.in',    '9876543215', '89 Connaught Place',  'Delhi'),
(7,  'Kabir Malhotra',     'HR',          62000.00, 'kabir.malhotra@company.in',  '9876543216', '34 Sector 17',        'Punjab'),
(8,  'Meera Joshi',        'Marketing',   70000.00, 'meera.joshi@company.in',     '9876543217', '67 FC Road',          'Maharashtra'),
(9,  'Neha Nair',          'Finance',     73000.00, 'neha.nair@company.in',       '9876543218', '90 MG Road',          'Kerala'),
(10, 'Rohan Rao',          'Engineering', 91000.00, 'rohan.rao@company.in',       '9876543219', '12 Koramangala',      'Karnataka'),
(11, 'Reyansh Sen',        'HR',          60000.00, 'reyansh.sen@company.in',     '9876543220', '45 Park Street',      'West Bengal'),
(12, 'Saanvi Choudhury',   'Operations',  67000.00, 'saanvi.choudhury@company.in','9876543221', '78 Paltan Bazaar',    'Assam'),
(13, 'Shreya Chatterjee',  'Engineering', 85000.00, 'shreya.chatterjee@company.in','9876543222','23 Salt Lake',        'West Bengal'),
(14, 'Vivaan Saxena',      'Operations',  64000.00, 'vivaan.saxena@company.in',   '9876543223', '56 Civil Lines',      'Rajasthan'),
(15, 'Yash Deshmukh',      'Marketing',   69000.00, 'yash.deshmukh@company.in',   '9876543224', '89 Shivaji Nagar',    'Maharashtra'),
(16, 'Aisha Kulkarni',     'Finance',     76000.00, 'aisha.kulkarni@company.in',  '9876543225', '34 Deccan Gymkhana',  'Maharashtra');

-- Seed tasks
INSERT INTO employee_tasks (employee_name, task_title, task_description, deadline, status) VALUES
('Vihaan Patel',      'API Integration',       'Integrate payment gateway REST API',         '2026-07-01', 'IN_PROGRESS'),
('Vihaan Patel',      'Unit Testing',          'Write JUnit tests for DAO layer',            '2026-07-10', 'PENDING'),
('Aditya Verma',      'Database Migration',    'Migrate legacy tables to new schema',        '2026-06-30', 'COMPLETED'),
('Ananya Iyer',       'Campaign Report',       'Prepare Q2 marketing campaign results',      '2026-07-05', 'PENDING'),
('Diya Reddy',        'Budget Review',         'Review and finalize Q3 department budgets',   '2026-07-15', 'PENDING'),
('Ishaan Gupta',      'Code Review',           'Review pull requests for auth module',        '2026-06-28', 'IN_PROGRESS'),
('Kabir Malhotra',    'Onboarding Plan',       'Draft onboarding checklist for new hires',    '2026-07-08', 'PENDING'),
('Rohan Rao',         'Performance Tuning',    'Optimize slow database queries',              '2026-07-12', 'IN_PROGRESS'),
('Shreya Chatterjee', 'Feature Development',   'Build employee export to CSV feature',        '2026-07-20', 'PENDING'),
('Meera Joshi',       'Social Media Strategy', 'Plan social media calendar for July',         '2026-06-25', 'COMPLETED');

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
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Seed users
-- All default employee passwords are "employee123" -> hashed as SHA-256: e3817e0e2e3f1cfa62e553da4bbd80cce7e00ddad3a6e35a9cb37c59d9edf03f
-- Admin password is "admin123" -> hashed as SHA-256: 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9
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

-- Seed employees (linked to their user accounts)
INSERT INTO employees (user_id, name, department, salary, email, phone) VALUES
(1,  'Aarav Sharma',       'Management',  95000.00, 'aarav.sharma@company.in',    '9876543210'),
(2,  'Vihaan Patel',       'Engineering', 75000.00, 'vihaan.patel@company.in',    '9876543211'),
(3,  'Aditya Verma',       'Engineering', 82000.00, 'aditya.verma@company.in',    '9876543212'),
(4,  'Ananya Iyer',        'Marketing',   65000.00, 'ananya.iyer@company.in',     '9876543213'),
(5,  'Diya Reddy',         'Finance',     78000.00, 'diya.reddy@company.in',      '9876543214'),
(6,  'Ishaan Gupta',       'Engineering', 88000.00, 'ishaan.gupta@company.in',    '9876543215'),
(7,  'Kabir Malhotra',     'HR',          62000.00, 'kabir.malhotra@company.in',  '9876543216'),
(8,  'Meera Joshi',        'Marketing',   70000.00, 'meera.joshi@company.in',     '9876543217'),
(9,  'Neha Nair',          'Finance',     73000.00, 'neha.nair@company.in',       '9876543218'),
(10, 'Rohan Rao',          'Engineering', 91000.00, 'rohan.rao@company.in',       '9876543219'),
(11, 'Reyansh Sen',        'HR',          60000.00, 'reyansh.sen@company.in',     '9876543220'),
(12, 'Saanvi Choudhury',   'Operations',  67000.00, 'saanvi.choudhury@company.in','9876543221'),
(13, 'Shreya Chatterjee',  'Engineering', 85000.00, 'shreya.chatterjee@company.in','9876543222'),
(14, 'Vivaan Saxena',      'Operations',  64000.00, 'vivaan.saxena@company.in',   '9876543223'),
(15, 'Yash Deshmukh',      'Marketing',   69000.00, 'yash.deshmukh@company.in',   '9876543224'),
(16, 'Aisha Kulkarni',     'Finance',     76000.00, 'aisha.kulkarni@company.in',  '9876543225');

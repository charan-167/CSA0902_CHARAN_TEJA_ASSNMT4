-- ============================================================
-- CAMPUS EVENT REGISTRATION DATABASE SCHEMA
-- Target DBMS: MySQL 8.0+
-- Course: CSA09 Programming in Java (CO4 Assignment 4, Task 6)
-- ============================================================

DROP DATABASE IF EXISTS campus_events_db;
CREATE DATABASE campus_events_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE campus_events_db;

-- ------------------------------------------------------------
-- Table structure for table `event_registrations`
-- ------------------------------------------------------------
CREATE TABLE event_registrations (
    registration_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(20) NOT NULL,
    student_name VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL,
    event_name VARCHAR(100) NOT NULL,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Business Rule: A student cannot register twice for the same event
    CONSTRAINT uq_student_event UNIQUE (student_id, event_name)
) ENGINE=InnoDB;

-- Indexes to optimise event filtering and fast pagination
CREATE INDEX idx_event_name ON event_registrations (event_name);
CREATE INDEX idx_student_id ON event_registrations (student_id);

-- ------------------------------------------------------------
-- Seed Data for Testing and Verification
-- ------------------------------------------------------------
INSERT INTO event_registrations (student_id, student_name, department, event_name) VALUES
('2024CS101', 'Aarav Sharma', 'Computer Science', 'Codeathon'),
('2024CS105', 'Bhavna Verma', 'Computer Science', 'Web Design Hackathon'),
('2024EC201', 'Chetan Kumar', 'Electronics', 'RoboWars'),
('2024ME304', 'Divya Nair', 'Mechanical', 'AutoCAD Design Expo'),
('2024CS109', 'Eshan Gupta', 'Computer Science', 'Codeathon'),
('2024IT112', 'Farhan Ali', 'Information Tech', 'Quiz Bowl'),
('2024EE205', 'Gitanjali Rao', 'Electrical', 'RoboWars');

-- ------------------------------------------------------------
-- Sample Verification Queries
-- ------------------------------------------------------------
-- 1. View all registrations
SELECT * FROM event_registrations ORDER BY registered_at DESC;

-- 2. Filter by specific event
SELECT * FROM event_registrations WHERE event_name = 'Codeathon';

-- 3. Paginated query with LIMIT and OFFSET (demonstrating SwingWorker pagination)
SELECT * FROM event_registrations ORDER BY registration_id ASC LIMIT 5 OFFSET 0;

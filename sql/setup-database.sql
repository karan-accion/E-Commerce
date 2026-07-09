-- Week 6: E-Commerce Database Setup Script
-- Run this script in phpMyAdmin or MySQL client to create the database and tables

-- Create the database
CREATE DATABASE IF NOT EXISTS ecommerce_db;

-- Select the database
USE ecommerce_db;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert sample data (optional)
INSERT INTO users (name, email) VALUES 
('Alice Johnson', 'alice@example.com'),
('Bob Smith', 'bob@example.com'),
('Carol White', 'carol@example.com');

-- Verify the table was created and populated
SELECT * FROM users;

-- Show table structure
DESCRIBE users;

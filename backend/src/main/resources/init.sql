-- Database initialization script
-- Run this to populate initial data

-- Fine Categories
INSERT INTO fine_categories (code, description, amount, violation_type, created_at) VALUES
('SPEED001', 'Exceeding speed limit by 10-20 km/h', 1000.00, 'Speeding', NOW()),
('SPEED002', 'Exceeding speed limit by 20-40 km/h', 2500.00, 'Speeding', NOW()),
('RED001', 'Running red light', 3000.00, 'Traffic Signal Violation', NOW()),
('PARK001', 'Illegal parking', 1500.00, 'Parking Violation', NOW()),
('BELT001', 'Not wearing seat belt', 1000.00, 'Safety Violation', NOW()),
('HELM001', 'Not wearing helmet', 1500.00, 'Safety Violation', NOW()),
('DRINK001', 'Drunk driving', 5000.00, 'Dangerous Driving', NOW());

-- Sample Admin User (password: Admin@123)
INSERT INTO users (email, password, full_name, phone, role, district, active, created_at, updated_at) VALUES
('admin@slpolice.lk', '$2a$10$Ua5C0nKLLjKjEz5d9.1LK.nG9dP9Y0dP0vJdMzDd5r5P2d3d1d9m.', 'Admin User', '0112345678', 'ADMIN', 'Colombo', true, NOW(), NOW());

-- Sample Officer User (password: Officer@123)
INSERT INTO users (email, password, full_name, phone, role, district, active, created_at, updated_at) VALUES
('officer@slpolice.lk', '$2a$10$Ua5C0nKLLjKjEz5d9.1LK.nG9dP9Y0dP0vJdMzDd5r5P2d3d1d9m.', 'Officer User', '0712345678', 'OFFICER', 'Colombo', true, NOW(), NOW());

-- Sample Driver User (password: Driver@123)
INSERT INTO users (email, password, full_name, phone, role, license_number, vehicle_registration, district, active, created_at, updated_at) VALUES
('driver@example.com', '$2a$10$Ua5C0nKLLjKjEz5d9.1LK.nG9dP9Y0dP0vJdMzDd5r5P2d3d1d9m.', 'Driver User', '0777777777', 'DRIVER', 'DL001', 'ABC-1234', 'Colombo', true, NOW(), NOW());

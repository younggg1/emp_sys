CREATE TABLE IF NOT EXISTS history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    year INT NOT NULL,
    total_students INT NOT NULL,
    employed_students INT NOT NULL,
    employment_rate DECIMAL(5,2) NOT NULL,
    avg_salary DECIMAL(10,2) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_year (year)
); 
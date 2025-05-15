CREATE DATABASE IF NOT EXISTS secspe_empsys;
USE secspe_empsys;

-- 创建学校表
CREATE TABLE schools (
    school_id BIGINT PRIMARY KEY,
    school_name VARCHAR(100) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE (school_name)
);

-- 创建用户表
CREATE TABLE users (
    user_id BIGINT PRIMARY KEY,
    school_id BIGINT NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('student', 'counselor', 'admin') NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (school_id) REFERENCES schools(school_id) ON DELETE RESTRICT,
    UNIQUE (school_id, username),
    INDEX idx_school_id (school_id)
);

-- 创建学生信息表
CREATE TABLE students (
    student_id BIGINT PRIMARY KEY,
    school_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    class_name VARCHAR(50) NOT NULL,
    college VARCHAR(50) NOT NULL,
    major VARCHAR(50) NOT NULL,
    counselor_id BIGINT NOT NULL,
    employment_status ENUM('unemployed', 'employed') NOT NULL DEFAULT 'unemployed',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (school_id) REFERENCES schools(school_id) ON DELETE RESTRICT,
    FOREIGN KEY (counselor_id) REFERENCES users(user_id) ON DELETE RESTRICT,
    INDEX idx_school_id (school_id),
    INDEX idx_counselor_id (counselor_id)
);

-- 创建辅导员信息表
CREATE TABLE counselors (
    counselor_id BIGINT PRIMARY KEY,
    school_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (counselor_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (school_id) REFERENCES schools(school_id) ON DELETE RESTRICT,
    INDEX idx_school_id (school_id)
);

-- 创建就业信息表
CREATE TABLE employment_records (
    record_id BIGINT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    major VARCHAR(50) NOT NULL,
    major_category ENUM('science', 'humanities', 'sports', 'arts') NOT NULL,
    company_nature VARCHAR(50) NOT NULL,
    company VARCHAR(100) NOT NULL,
    position VARCHAR(50) NOT NULL,
    salary DECIMAL(10,2) NOT NULL,
    entry_date DATE NOT NULL,
    region VARCHAR(50) NOT NULL,
    status ENUM('pending', 'approved') NOT NULL DEFAULT 'pending',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    INDEX idx_student_id (student_id),
    INDEX idx_region (region)
);

-- 创建反馈信息表
CREATE TABLE feedback_records (
    feedback_id BIGINT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    stage VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    status ENUM('pending', 'approved') NOT NULL DEFAULT 'pending',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    INDEX idx_student_id (student_id)
);

-- 创建权限表
CREATE TABLE permissions (
    permission_id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    can_edit BOOLEAN NOT NULL DEFAULT FALSE,
    can_delete BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id)
);

-- 创建系统设置表
CREATE TABLE system_settings (
    setting_id BIGINT PRIMARY KEY,
    school_id BIGINT NOT NULL,
    require_captcha BOOLEAN NOT NULL DEFAULT TRUE,
    require_approval BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (school_id) REFERENCES schools(school_id) ON DELETE RESTRICT,
    UNIQUE (school_id)
);
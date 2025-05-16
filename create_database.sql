CREATE DATABASE IF NOT EXISTS employment_survey;
USE employment_survey;

-- 用户表
-- 说明：存储所有用户（学生、辅导员、管理员）的登录信息，主键自增
CREATE TABLE users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('student', 'counselor', 'admin') NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 学生信息表
-- 说明：存储学生详细信息，姓名和专业在此表维护，主键自增
CREATE TABLE students (
    student_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    grade VARCHAR(10) NOT NULL,
    class_name VARCHAR(50) NOT NULL,
    college VARCHAR(50) NOT NULL,
    major VARCHAR(50) NOT NULL,
    counselor_id BIGINT NOT NULL,
    employment_status ENUM('unemployed', 'employed') NOT NULL DEFAULT 'unemployed',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (counselor_id) REFERENCES users(user_id) ON DELETE RESTRICT,
    INDEX idx_counselor_id (counselor_id),
    INDEX idx_grade (grade)
);

-- 辅导员信息表
-- 说明：存储辅导员信息，关联users表，主键自增
CREATE TABLE counselors (
    counselor_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (counselor_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 就业信息表
-- 说明：存储学生就业信息，去掉name和major（从students表获取），学生只能登记和查看自己的记录（通过student_id限制），主键自增
CREATE TABLE employment_records (
    record_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
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

-- 反馈信息表
-- 说明：存储学生反馈，学生只能登记和查看自己的反馈（通过student_id限制），需已就业，主键自增
CREATE TABLE feedback_records (
    feedback_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    stage VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    status ENUM('pending', 'approved') NOT NULL DEFAULT 'pending',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    INDEX idx_student_id (student_id)
);

-- 权限表
-- 说明：存储辅导员权限，控制本班数据的编辑和删除，主键自增
CREATE TABLE permissions (
    permission_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    can_edit BOOLEAN NOT NULL DEFAULT FALSE,
    can_delete BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id)
);

-- 系统设置表
-- 说明：控制验证码和审核开关，主键自增
CREATE TABLE system_settings (
    setting_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    require_captcha BOOLEAN NOT NULL DEFAULT TRUE,
    require_approval BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 插入测试数据
INSERT INTO system_settings (require_captcha, require_approval) VALUES (TRUE, TRUE);
INSERT INTO users (username, password, role) VALUES ('admin1', '123456', 'admin');
INSERT INTO users (username, password, role) VALUES ('counselor1', '123456', 'counselor');
INSERT INTO counselors (name, counselor_id) VALUES ('张老师', LAST_INSERT_ID());
INSERT INTO users (username, password, role) VALUES ('student1', '123456', 'student');
INSERT INTO students (name, grade, class_name, college, major, counselor_id, employment_status)
VALUES ('李明', '2023', '计算机1班', '计算机学院', '计算机科学', 2, 'unemployed');
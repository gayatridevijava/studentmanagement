CREATE SCHEMA IF NOT EXISTS sms;

USE sms;

CREATE TABLE IF NOT EXISTS sms.course (
    course_id       INT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(150) NOT NULL,
    credits         INT DEFAULT 3,
    description     TEXT,
    status          ENUM('Active', 'Inactive') DEFAULT 'Active',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by      VARCHAR(100),
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by      VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS sms.student (
    student_id      INT AUTO_INCREMENT PRIMARY KEY,
    first_name      VARCHAR(50) NOT NULL,
    last_name       VARCHAR(50) NOT NULL,
    email           VARCHAR(100) UNIQUE NOT NULL,
    phone           VARCHAR(20) UNIQUE,
    date_of_birth   DATE,
    gender          CHAR(1) CHECK (gender IN ('M', 'F', 'O')),
    address         TEXT,
    enrollment_date DATE DEFAULT (CURRENT_DATE),
    status          ENUM('Active', 'Inactive') DEFAULT 'Active',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by      VARCHAR(100),
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by      VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS sms.enrollments (
    enrollment_id     INT AUTO_INCREMENT PRIMARY KEY,
    student_id        INT,
    course_id         INT,
    enrollment_date   DATE DEFAULT (CURRENT_DATE),
    status            ENUM('Active', 'Cancelled', 'Completed') DEFAULT 'Active',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by      VARCHAR(100),
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by      VARCHAR(100),

    UNIQUE KEY (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES student(student_id),
    FOREIGN KEY (course_id)  REFERENCES course(course_id)


);

CREATE TABLE IF NOT EXISTS users (
    user_id             INT AUTO_INCREMENT PRIMARY KEY,
    username            VARCHAR(50) NOT NULL UNIQUE,
    email               VARCHAR(100) NOT NULL UNIQUE,
    password_hash       VARCHAR(255) NOT NULL,
    first_name          VARCHAR(50) NOT NULL,
    last_name           VARCHAR(50) NOT NULL,
    phone               VARCHAR(20),
    status              ENUM('Active', 'Inactive') DEFAULT 'Active',
    created_at          DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by          VARCHAR(100),
    updated_at          DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by          VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS roles (
    role_id             INT AUTO_INCREMENT PRIMARY KEY,
    role_name           VARCHAR(50) NOT NULL UNIQUE,
    description         TEXT,
    status              ENUM('Active', 'Inactive') DEFAULT 'Active',
    created_at          DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by          VARCHAR(100),
    updated_at          DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by          VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS user_role (
    user_role_id        INT AUTO_INCREMENT PRIMARY KEY,
    user_id             INT NOT NULL,
    role_id             INT NOT NULL,
    assigned_at         DATETIME DEFAULT CURRENT_TIMESTAMP,
    assigned_by         VARCHAR(100),


    CONSTRAINT fk_ur_user  FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_ur_role  FOREIGN KEY (role_id) REFERENCES roles(role_id),
    CONSTRAINT uq_user_role UNIQUE (user_id, role_id)

);


INSERT INTO roles (role_name, description, created_by) VALUES
('Admin',    'Full system access',               'system'),
('Teacher',  'Manage courses and view students', 'system');


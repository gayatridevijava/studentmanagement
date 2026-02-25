INSERT INTO sms.users
(username, email, password_hash, first_name, last_name, phone, status, created_by)
VALUES
('admin.gayatri',   'gayatridevi.admin@sms.com',   'hashed_password_1', 'Gayatri',  'Marella',   '9001234561', 'Active', 'system'),
('teacher.one', 'teacher1@sms.com', 'hashed_password_2', 'Teacher ONE',  'Test', '9001234562', 'Active', 'system'),
('teacher.two', 'teacher2@sms.com', 'hashed_password_3', 'TeacherTWO',  'TEST','9001234563', 'Active', 'system');



INSERT INTO sms.user_role (user_id, role_id, assigned_by)
VALUES
(
    (SELECT user_id FROM sms.users WHERE username = 'admin.gayatri'),
    (SELECT role_id FROM sms.roles WHERE role_name = 'Admin'),
    'system'
),
(
    (SELECT user_id FROM sms.users WHERE username = 'teacher.one'),
    (SELECT role_id FROM sms.roles WHERE role_name = 'Teacher'),
    'system'
),
(
    (SELECT user_id FROM sms.users WHERE username = 'teacher.two'),
    (SELECT role_id FROM sms.roles WHERE role_name = 'Teacher'),
    'system'
);

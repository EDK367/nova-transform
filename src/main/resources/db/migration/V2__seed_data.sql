-- ============================================================
-- ROLES
-- ============================================================
INSERT INTO roles (name, description, created_at, updated_at)
VALUES ('ADMIN', 'System Administrator', NOW(), NOW()),
       ('ENTRENADOR', 'Gym Trainer', NOW(), NOW()),
       ('RECEPCIONISTA', 'Gym Receptionist', NOW(), NOW()),
       ('CLIENTE', 'Gym Customer', NOW(), NOW());

-- ============================================================
-- USERS
-- ============================================================
INSERT INTO users (id,
                   id_role,
                   first_name,
                   last_name,
                   email,
                   username,
                   password,
                   active,
                   two_factor_auth,
                   created_at,
                   updated_at)
VALUES (gen_random_uuid(),
        (SELECT id FROM roles WHERE name = 'ADMIN'),
        'System',
        'Admin',
        'admin@solairy.com',
        'admin',
        '$2a$10$5ObnwRfhZWYW6Y6/v1S92eOwCWPHYgMi1KL4RbUSAiVk6KDfJSD6q',
        TRUE,
        FALSE,
        NOW(),
        NOW());


INSERT INTO users (id,
                   id_role,
                   first_name,
                   last_name,
                   email,
                   username,
                   password,
                   active,
                   two_factor_auth,
                   created_at,
                   updated_at)
VALUES (gen_random_uuid(),
        (SELECT id FROM roles WHERE name = 'CLIENTE'),
        'John',
        'Doe',
        'john.doe@nova-transform.com',
        'jdoe',
        '$2a$10$5ObnwRfhZWYW6Y6/v1S92eOwCWPHYgMi1KL4RbUSAiVk6KDfJSD6q',
        TRUE,
        FALSE,
        NOW(),
        NOW());

INSERT INTO roles (id, name, description, created_at, updated_at)
VALUES (nextval('roles_seq'), 'ADMIN', 'System Administrator', NOW(), NOW());

INSERT INTO roles (id, name, description, created_at, updated_at)
VALUES (nextval('roles_seq'), 'USER', 'Standard User', NOW(), NOW());

-- Password: Admin123!
INSERT INTO users (id, first_name, last_name, email, username, password, is_active, created_at, updated_at, role_id)
VALUES (
           gen_random_uuid(),
           'System', 'Admin', 'admin@nova-transform.com', 'admin',
           '$2a$10$PYPvsjogJjjAj4amIibEoObCM5A20lP30BFM7DoDYV.04X9l4Q1oe',
           TRUE, NOW(), NOW(), (SELECT id FROM roles WHERE name = 'ADMIN')
       );

-- Password: User123!
INSERT INTO users (id, first_name, last_name, email, username, password, is_active, created_at, updated_at, role_id)
VALUES (
           gen_random_uuid(),
           'John', 'Doe', 'john.doe@nova-transform.com', 'jdoe',
           '$2a$10$PYPvsjogJjjAj4amIibEoObCM5A20lP30BFM7DoDYV.04X9l4Q1oe',
           TRUE, NOW(), NOW(), (SELECT id FROM roles WHERE name = 'USER')
       );
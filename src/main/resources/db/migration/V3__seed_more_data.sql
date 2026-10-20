INSERT INTO branches (name, address, phone, active, created_at, updated_at)
VALUES ('Sede Central', 'Av. Reforma 100, Zona 10', '+502 2200 0001', TRUE, NOW(), NOW()),
       ('Sede Norte', 'Blvd. Los Próceres 250, Zona 15', '+502 2200 0002', TRUE, NOW(), NOW()),
       ('Sede Sur', 'Calz. Roosevelt 450, Zona 11', '+502 2200 0003', TRUE, NOW(), NOW()),
       ('Sede Poniente', 'Carretera a El Salvador Km 14', '+502 2200 0004', TRUE, NOW(), NOW());

INSERT INTO users (id, id_role, id_branch, first_name, last_name, email, phone, username, password, active, two_factor_auth, created_at, updated_at)
VALUES ('a1111111-1111-1111-1111-111111111111', (SELECT id FROM roles WHERE name = 'ENTRENADOR'), 1, 'Alejandro', 'Vargas', 'alejandro.vargas@nova.com', '+502 5500 0001', 'trainer1', '$2a$10$5ObnwRfhZWYW6Y6/v1S92eOwCWPHYgMi1KL4RbUSAiVk6KDfJSD6q', TRUE, FALSE, NOW(), NOW()),
       ('a2222222-2222-2222-2222-222222222222', (SELECT id FROM roles WHERE name = 'ENTRENADOR'), 2, 'Beatriz', 'Morales', 'beatriz.morales@nova.com', '+502 5500 0002', 'trainer2', '$2a$10$5ObnwRfhZWYW6Y6/v1S92eOwCWPHYgMi1KL4RbUSAiVk6KDfJSD6q', TRUE, FALSE, NOW(), NOW()),
       ('a3333333-3333-3333-3333-333333333333', (SELECT id FROM roles WHERE name = 'ENTRENADOR'), 3, 'Carlos', 'Guerrero', 'carlos.guerrero@nova.com', '+502 5500 0003', 'trainer3', '$2a$10$5ObnwRfhZWYW6Y6/v1S92eOwCWPHYgMi1KL4RbUSAiVk6KDfJSD6q', TRUE, FALSE, NOW(), NOW()),
       ('a4444444-4444-4444-4444-444444444444', (SELECT id FROM roles WHERE name = 'ENTRENADOR'), 4, 'Diana', 'Castro', 'diana.castro@nova.com', '+502 5500 0004', 'trainer4', '$2a$10$5ObnwRfhZWYW6Y6/v1S92eOwCWPHYgMi1KL4RbUSAiVk6KDfJSD6q', TRUE, FALSE, NOW(), NOW()),
       ('b1111111-1111-1111-1111-111111111111', (SELECT id FROM roles WHERE name = 'RECEPCIONISTA'), 1, 'Elena', 'Torres', 'elena.torres@nova.com', '+502 5500 0005', 'recep1', '$2a$10$5ObnwRfhZWYW6Y6/v1S92eOwCWPHYgMi1KL4RbUSAiVk6KDfJSD6q', TRUE, FALSE, NOW(), NOW()),
       ('b2222222-2222-2222-2222-222222222222', (SELECT id FROM roles WHERE name = 'RECEPCIONISTA'), 2, 'Fernando', 'Luna', 'fernando.luna@nova.com', '+502 5500 0006', 'recep2', '$2a$10$5ObnwRfhZWYW6Y6/v1S92eOwCWPHYgMi1KL4RbUSAiVk6KDfJSD6q', TRUE, FALSE, NOW(), NOW()),
       ('b3333333-3333-3333-3333-333333333333', (SELECT id FROM roles WHERE name = 'RECEPCIONISTA'), 3, 'Gabriela', 'Rios', 'gabriela.rios@nova.com', '+502 5500 0007', 'recep3', '$2a$10$5ObnwRfhZWYW6Y6/v1S92eOwCWPHYgMi1KL4RbUSAiVk6KDfJSD6q', TRUE, FALSE, NOW(), NOW()),
       ('b4444444-4444-4444-4444-444444444444', (SELECT id FROM roles WHERE name = 'RECEPCIONISTA'), 4, 'Hugo', 'Paredes', 'hugo.paredes@nova.com', '+502 5500 0008', 'recep4', '$2a$10$5ObnwRfhZWYW6Y6/v1S92eOwCWPHYgMi1KL4RbUSAiVk6KDfJSD6q', TRUE, FALSE, NOW(), NOW()),
       ('c1111111-1111-1111-1111-111111111111', (SELECT id FROM roles WHERE name = 'CLIENTE'), 1, 'Isabel', 'Navarro', 'isabel.navarro@nova.com', '+502 5500 0009', 'member1', '$2a$10$5ObnwRfhZWYW6Y6/v1S92eOwCWPHYgMi1KL4RbUSAiVk6KDfJSD6q', TRUE, FALSE, NOW(), NOW()),
       ('c2222222-2222-2222-2222-222222222222', (SELECT id FROM roles WHERE name = 'CLIENTE'), 2, 'Jorge', 'Serrano', 'jorge.serrano@nova.com', '+502 5500 0010', 'member2', '$2a$10$5ObnwRfhZWYW6Y6/v1S92eOwCWPHYgMi1KL4RbUSAiVk6KDfJSD6q', TRUE, FALSE, NOW(), NOW()),
       ('c3333333-3333-3333-3333-333333333333', (SELECT id FROM roles WHERE name = 'CLIENTE'), 3, 'Karla', 'Mendoza', 'karla.mendoza@nova.com', '+502 5500 0011', 'member3', '$2a$10$5ObnwRfhZWYW6Y6/v1S92eOwCWPHYgMi1KL4RbUSAiVk6KDfJSD6q', TRUE, FALSE, NOW(), NOW()),
       ('c4444444-4444-4444-4444-444444444444', (SELECT id FROM roles WHERE name = 'CLIENTE'), 4, 'Luis', 'Ortega', 'luis.ortega@nova.com', '+502 5500 0012', 'member4', '$2a$10$5ObnwRfhZWYW6Y6/v1S92eOwCWPHYgMi1KL4RbUSAiVk6KDfJSD6q', TRUE, FALSE, NOW(), NOW()),
       ('c5555555-5555-5555-5555-555555555555', (SELECT id FROM roles WHERE name = 'CLIENTE'), 1, 'Mariana', 'Lopez', 'mariana.lopez@nova.com', '+502 5500 0013', 'member5', '$2a$10$5ObnwRfhZWYW6Y6/v1S92eOwCWPHYgMi1KL4RbUSAiVk6KDfJSD6q', TRUE, FALSE, NOW(), NOW()),
       ('c6666666-6666-6666-6666-666666666666', (SELECT id FROM roles WHERE name = 'CLIENTE'), 2, 'Roberto', 'Castillo', 'roberto.castillo@nova.com', '+502 5500 0014', 'member6', '$2a$10$5ObnwRfhZWYW6Y6/v1S92eOwCWPHYgMi1KL4RbUSAiVk6KDfJSD6q', TRUE, FALSE, NOW(), NOW());

INSERT INTO trainers (id_trainer, hire_date, created_at, updated_at)
VALUES ('a1111111-1111-1111-1111-111111111111', '2024-01-10', NOW(), NOW()),
       ('a2222222-2222-2222-2222-222222222222', '2024-02-15', NOW(), NOW()),
       ('a3333333-3333-3333-3333-333333333333', '2024-03-20', NOW(), NOW()),
       ('a4444444-4444-4444-4444-444444444444', '2024-04-25', NOW(), NOW());

INSERT INTO receptionists (id_receptionist, hire_date, created_at, updated_at)
VALUES ('b1111111-1111-1111-1111-111111111111', '2024-01-12', NOW(), NOW()),
       ('b2222222-2222-2222-2222-222222222222', '2024-02-18', NOW(), NOW()),
       ('b3333333-3333-3333-3333-333333333333', '2024-03-22', NOW(), NOW()),
       ('b4444444-4444-4444-4444-444444444444', '2024-04-28', NOW(), NOW());

INSERT INTO members (id_member, registration_date, created_at, updated_at)
VALUES ('c1111111-1111-1111-1111-111111111111', '2024-05-01', NOW(), NOW()),
       ('c2222222-2222-2222-2222-222222222222', '2024-05-05', NOW(), NOW()),
       ('c3333333-3333-3333-3333-333333333333', '2024-05-10', NOW(), NOW()),
       ('c4444444-4444-4444-4444-444444444444', '2024-05-15', NOW(), NOW()),
       ('c5555555-5555-5555-5555-555555555555', '2024-06-01', NOW(), NOW()),
       ('c6666666-6666-6666-6666-666666666666', '2024-06-10', NOW(), NOW());

INSERT INTO members (id_member, registration_date, created_at, updated_at)
SELECT id, '2024-04-01', NOW(), NOW()
FROM users
WHERE username = 'jdoe'
ON CONFLICT (id_member) DO NOTHING;

INSERT INTO benefits (name, description, created_at, updated_at)
VALUES ('Acceso Área de Pesas', 'Uso ilimitado del área de pesas libres y máquinas integradas', NOW(), NOW()),
       ('Clases Grupales', 'Acceso a todas las clases grupales programadas en la semana', NOW(), NOW()),
       ('Sauna y Vapor', 'Uso de las instalaciones húmedas de relajación', NOW(), NOW()),
       ('Acceso 24/7 Multi-Sede', 'Entrada sin restricción de horario a todas las sucursales', NOW(), NOW()),
       ('Plan Nutricional', 'Guía y seguimiento nutricional personalizado mensual', NOW(), NOW()),
       ('Evaluación Antropométrica InBody', 'Medición mensual de composición corporal y % grasa', NOW(), NOW());

INSERT INTO membership_plans (name, duration, price, description, created_at, updated_at)
VALUES ('Plan Básico', 30, 250.00, 'Acceso básico mensual para entrenamiento individual', NOW(), NOW()),
       ('Plan Fitness Total', 30, 450.00, 'Acceso mensual con clases grupales incluidas', NOW(), NOW()),
       ('Plan Trimestral Pro', 90, 1100.00, 'Acceso completo trimestral con sauna y nutrición incluidos', NOW(), NOW()),
       ('Plan Anual VIP Elite', 365, 3800.00, 'Membresía anual con todos los beneficios y acceso 24/7 multi-sede', NOW(), NOW());

INSERT INTO benefit_plans (id_plan, id_benefit, created_at, updated_at)
VALUES (1, 1, NOW(), NOW()),
       (2, 1, NOW(), NOW()),
       (2, 2, NOW(), NOW()),
       (3, 1, NOW(), NOW()),
       (3, 2, NOW(), NOW()),
       (3, 3, NOW(), NOW()),
       (3, 5, NOW(), NOW()),
       (4, 1, NOW(), NOW()),
       (4, 2, NOW(), NOW()),
       (4, 3, NOW(), NOW()),
       (4, 4, NOW(), NOW()),
       (4, 5, NOW(), NOW()),
       (4, 6, NOW(), NOW());

INSERT INTO plan_price_histories (id_plan, price, start_date, end_date, observation, created_at, updated_at)
VALUES (1, 225.00, '2024-01-01', '2024-12-31', 'Tarifa inicial', NOW(), NOW()),
       (2, 400.00, '2024-01-01', '2024-12-31', 'Tarifa anterior', NOW(), NOW()),
       (3, 999.00, '2024-01-01', '2024-12-31', 'Promoción de apertura', NOW(), NOW()),
       (4, 3500.00, '2024-01-01', '2024-12-31', 'Precio de lanzamiento', NOW(), NOW());

INSERT INTO promotions (name, description, discount_type, discount_value, start_date, end_date, active, created_at, updated_at)
VALUES ('Promo Verano', '15% de descuento en planes trimestrales', 'PERCENTAGE', 15.00, '2025-01-01', '2026-12-31', TRUE, NOW(), NOW()),
       ('Promo Anual VIP', '20% de descuento en plan anual', 'PERCENTAGE', 20.00, '2025-01-01', '2026-12-31', TRUE, NOW(), NOW()),
       ('Bono Bienvenida', 'Descuento directo de 50 Quetzales', 'FIXED_AMOUNT', 50.00, '2025-01-01', '2026-12-31', TRUE, NOW(), NOW()),
       ('Convenio Estudiantes', '10% de descuento permanente', 'PERCENTAGE', 10.00, '2025-01-01', '2026-12-31', TRUE, NOW(), NOW());

INSERT INTO memberships (id_member, id_plan, status, start_date, due_date, created_at, updated_at)
VALUES ('c1111111-1111-1111-1111-111111111111', 1, 'ACTIVE', '2025-01-01', '2026-01-31', NOW(), NOW()),
       ('c2222222-2222-2222-2222-222222222222', 2, 'ACTIVE', '2025-01-01', '2026-01-31', NOW(), NOW()),
       ('c3333333-3333-3333-3333-333333333333', 3, 'ACTIVE', '2025-01-01', '2026-03-31', NOW(), NOW()),
       ('c4444444-4444-4444-4444-444444444444', 4, 'ACTIVE', '2025-01-01', '2026-12-31', NOW(), NOW()),
       ('c5555555-5555-5555-5555-555555555555', 2, 'ACTIVE', '2025-02-01', '2026-02-28', NOW(), NOW()),
       ('c6666666-6666-6666-6666-666666666666', 3, 'FROZEN', '2025-01-15', '2026-04-15', NOW(), NOW());

INSERT INTO membership_histories (id_membership, previous_status, new_status, change_date, observation, created_at, updated_at)
VALUES (1, 'INACTIVE', 'ACTIVE', NOW(), 'Activación inicial de membresía básica', NOW(), NOW()),
       (2, 'INACTIVE', 'ACTIVE', NOW(), 'Activación inicial de membresía fitness', NOW(), NOW()),
       (3, 'INACTIVE', 'ACTIVE', NOW(), 'Activación membresía trimestral pro', NOW(), NOW()),
       (4, 'INACTIVE', 'ACTIVE', NOW(), 'Activación membresía anual VIP elite', NOW(), NOW()),
       (5, 'INACTIVE', 'ACTIVE', NOW(), 'Activación inicial de socio 5', NOW(), NOW()),
       (6, 'ACTIVE', 'FROZEN', NOW(), 'Congelamiento temporal por viaje de trabajo', NOW(), NOW());

INSERT INTO membership_freezes (id_membership, start_date, end_date, reason, created_at, updated_at)
VALUES (1, '2025-02-01', '2025-02-15', 'Vacaciones laborales', NOW(), NOW()),
       (6, '2025-02-10', '2025-03-10', 'Viaje de trabajo internacional', NOW(), NOW());

INSERT INTO class_types (name, description, created_at, updated_at)
VALUES ('Spinning', 'Entrenamiento cardiovascular intensivo sobre bicicleta estacionaria', NOW(), NOW()),
       ('Crossfit', 'Entrenamiento funcional de alta intensidad con ejercicios variados', NOW(), NOW()),
       ('Yoga Vinyasa', 'Secuencias fluidas de posturas coordinadas con la respiración', NOW(), NOW()),
       ('Pilates Mat', 'Ejercicios en colchoneta enfocados en fuerza del core y estabilidad', NOW(), NOW()),
       ('Boxeo Funcional', 'Técnica de golpeo y circuito metabólico de alta quema calórica', NOW(), NOW()),
       ('HIIT Extreme', 'Intervalos cortos a máxima potencia para desarrollo aeróbico', NOW(), NOW());

INSERT INTO classes (id_branch, id_class_type, id_trainer, status, schedule, duration, max_capacity, created_at, updated_at)
VALUES (1, 1, 'a1111111-1111-1111-1111-111111111111', 'SCHEDULED', '2025-06-01 07:00:00', 60, 20, NOW(), NOW()),
       (2, 2, 'a2222222-2222-2222-2222-222222222222', 'SCHEDULED', '2025-06-01 08:30:00', 60, 15, NOW(), NOW()),
       (3, 3, 'a3333333-3333-3333-3333-333333333333', 'SCHEDULED', '2025-06-01 18:00:00', 60, 25, NOW(), NOW()),
       (4, 4, 'a4444444-4444-4444-4444-444444444444', 'SCHEDULED', '2025-06-01 19:30:00', 60, 15, NOW(), NOW()),
       (1, 5, 'a1111111-1111-1111-1111-111111111111', 'SCHEDULED', '2025-06-02 07:00:00', 60, 18, NOW(), NOW()),
       (2, 6, 'a2222222-2222-2222-2222-222222222222', 'SCHEDULED', '2025-06-02 18:30:00', 45, 20, NOW(), NOW());

INSERT INTO class_enrollments (id_class, id_member, status, enrollment_date, created_at, updated_at)
VALUES (1, 'c1111111-1111-1111-1111-111111111111', 'ENROLLED', NOW(), NOW(), NOW()),
       (2, 'c2222222-2222-2222-2222-222222222222', 'ENROLLED', NOW(), NOW(), NOW()),
       (3, 'c3333333-3333-3333-3333-333333333333', 'ENROLLED', NOW(), NOW(), NOW()),
       (4, 'c4444444-4444-4444-4444-444444444444', 'ENROLLED', NOW(), NOW(), NOW()),
       (5, 'c5555555-5555-5555-5555-555555555555', 'ENROLLED', NOW(), NOW(), NOW()),
       (6, 'c1111111-1111-1111-1111-111111111111', 'ENROLLED', NOW(), NOW(), NOW());

INSERT INTO class_ratings (id_enrollment, rating, comment, date_time, created_at, updated_at)
VALUES (1, 5, 'Excelente instructor y gran energía en la clase de spinning', NOW(), NOW(), NOW()),
       (2, 5, 'Entrenamiento exigente y bien guiado en crossfit', NOW(), NOW(), NOW()),
       (3, 4, 'Muy buena clase de yoga, relajante y dinámica', NOW(), NOW(), NOW()),
       (4, 5, 'Rutina de pilates muy bien explicada para todos los niveles', NOW(), NOW(), NOW()),
       (5, 5, 'Gran técnica de boxeo y excelente preparación física', NOW(), NOW(), NOW());

INSERT INTO attendances (id_branch, id_receptionist, id_member, date, entry_time, exit_time, created_at, updated_at)
VALUES (1, 'b1111111-1111-1111-1111-111111111111', 'c1111111-1111-1111-1111-111111111111', '2025-05-02', '07:05:00', '08:30:00', NOW(), NOW()),
       (2, 'b2222222-2222-2222-2222-222222222222', 'c2222222-2222-2222-2222-222222222222', '2025-05-06', '08:00:00', '09:45:00', NOW(), NOW()),
       (3, 'b3333333-3333-3333-3333-333333333333', 'c3333333-3333-3333-3333-333333333333', '2025-05-11', '18:15:00', '19:30:00', NOW(), NOW()),
       (4, 'b4444444-4444-4444-4444-444444444444', 'c4444444-4444-4444-4444-444444444444', '2025-05-16', '19:00:00', '20:15:00', NOW(), NOW()),
       (1, 'b1111111-1111-1111-1111-111111111111', 'c5555555-5555-5555-5555-555555555555', '2025-05-18', '06:30:00', '08:00:00', NOW(), NOW()),
       (2, 'b2222222-2222-2222-2222-222222222222', 'c1111111-1111-1111-1111-111111111111', '2025-05-20', '07:10:00', '08:40:00', NOW(), NOW());

INSERT INTO waitlists (id_class, id_member, registration_date, notified, created_at, updated_at)
VALUES (1, 'c2222222-2222-2222-2222-222222222222', NOW(), FALSE, NOW(), NOW()),
       (2, 'c3333333-3333-3333-3333-333333333333', NOW(), FALSE, NOW(), NOW()),
       (3, 'c4444444-4444-4444-4444-444444444444', NOW(), FALSE, NOW(), NOW()),
       (4, 'c1111111-1111-1111-1111-111111111111', NOW(), FALSE, NOW(), NOW());

INSERT INTO specializations (name, description, created_at, updated_at)
VALUES ('Hipertrofia Muscular', 'Especialización en desarrollo y ganancia de masa muscular', NOW(), NOW()),
       ('Pérdida de Grasa & Metabólico', 'Enfoque en déficit energético y entrenamiento de alta intensidad', NOW(), NOW()),
       ('Rehabilitación Deportiva', 'Recuperación y readaptación física post lesiones musculares y articulares', NOW(), NOW()),
       ('Calistenia y Fuerza Relativa', 'Control corporal y ejercicios gimnásticos avanzados', NOW(), NOW()),
       ('Crossfit y Rendimiento', 'Entrenamiento funcional de potencia y halterofilia', NOW(), NOW()),
       ('Nutrición Deportiva', 'Planificación de macros y suplementación en atletas', NOW(), NOW());

INSERT INTO trainer_specializations (id_trainer, id_specialization, created_at, updated_at)
VALUES ('a1111111-1111-1111-1111-111111111111', 1, NOW(), NOW()),
       ('a1111111-1111-1111-1111-111111111111', 5, NOW(), NOW()),
       ('a2222222-2222-2222-2222-222222222222', 2, NOW(), NOW()),
       ('a2222222-2222-2222-2222-222222222222', 6, NOW(), NOW()),
       ('a3333333-3333-3333-3333-333333333333', 3, NOW(), NOW()),
       ('a3333333-3333-3333-3333-333333333333', 4, NOW(), NOW()),
       ('a4444444-4444-4444-4444-444444444444', 4, NOW(), NOW()),
       ('a4444444-4444-4444-4444-444444444444', 1, NOW(), NOW());

INSERT INTO trainer_assignments (id_member, id_trainer, start_date, end_date, active, reason, created_at, updated_at)
VALUES ('c1111111-1111-1111-1111-111111111111', 'a1111111-1111-1111-1111-111111111111', '2025-01-05', NULL, TRUE, 'Asignación para plan de ganancia de masa muscular', NOW(), NOW()),
       ('c2222222-2222-2222-2222-222222222222', 'a2222222-2222-2222-2222-222222222222', '2025-01-05', NULL, TRUE, 'Asignación para reacondicionamiento metabólico', NOW(), NOW()),
       ('c3333333-3333-3333-3333-333333333333', 'a3333333-3333-3333-3333-333333333333', '2025-01-05', NULL, TRUE, 'Asignación para rehabilitación postural y movilidad', NOW(), NOW()),
       ('c4444444-4444-4444-4444-444444444444', 'a4444444-4444-4444-4444-444444444444', '2025-01-05', NULL, TRUE, 'Asignación para entrenamiento de fuerza máxima', NOW(), NOW()),
       ('c5555555-5555-5555-5555-555555555555', 'a1111111-1111-1111-1111-111111111111', '2025-02-01', NULL, TRUE, 'Acompañamiento en técnica y progresión', NOW(), NOW());

INSERT INTO exercises (name, description, muscle_group, created_at, updated_at)
VALUES ('Press de Banca Plano', 'Empuje horizontal con barra olímpica para pectoral mayor', 'Pecho', NOW(), NOW()),
       ('Sentadilla Trasera', 'Flexión de rodillas y cadera con barra para cuádriceps y glúteos', 'Pierna', NOW(), NOW()),
       ('Peso Muerto Convencional', 'Tracción vertical desde el suelo para toda la cadena posterior', 'Espalda', NOW(), NOW()),
       ('Press Militar con Mancuernas', 'Empuje vertical para desarrollo de deltoides', 'Hombro', NOW(), NOW()),
       ('Dominadas Pronas', 'Tracción vertical con peso corporal para dorsal ancho', 'Espalda', NOW(), NOW()),
       ('Fondos en Paralelas', 'Empuje en barras paralelas para pectoral inferior y tríceps', 'Pecho', NOW(), NOW()),
       ('Curl de Bíceps con Barra Z', 'Flexión de codo estricta para bíceps braquial', 'Bíceps', NOW(), NOW()),
       ('Extensión de Tríceps en Polea', 'Extensión aislada de codo con cuerda', 'Tríceps', NOW(), NOW()),
       ('Prensa de Piernas 45°', 'Empuje en máquina para cuádriceps y glúteos', 'Pierna', NOW(), NOW()),
       ('Elevaciones Laterales', 'Abducción de hombros con mancuernas para deltoides medio', 'Hombro', NOW(), NOW()),
       ('Remo con Barra', 'Tracción horizontal inclinada para grosor de espalda', 'Espalda', NOW(), NOW()),
       ('Plancha Abdominal Isométrica', 'Estabilidad del core y transverso del abdomen', 'Core / Abdomen', NOW(), NOW());

INSERT INTO routines (id_assignment, name, description, created_at, updated_at, active)
VALUES (1, 'Rutina Torso Hipertrofia', 'Enfoque en volumen e intensidad de pecho, espalda y hombro', NOW(), NOW(), TRUE),
       (2, 'Rutina Pierna Enfoque Fuerza', 'Sentadilla, prensa y trabajo accesorio de isquiotibiales', NOW(), NOW(), TRUE),
       (3, 'Rutina Push (Pecho, Hombro, Tríceps)', 'Patrones de empuje con progresión de sobrecarga', NOW(), NOW(), TRUE),
       (4, 'Rutina Pull (Espalda y Bíceps)', 'Patrones de tracción y densidad dorsal', NOW(), NOW(), TRUE),
       (5, 'Rutina Funcional Full Body', 'Circuito de fuerza, movilidad y resistencia', NOW(), NOW(), TRUE);

INSERT INTO routine_exercises (id_routine, id_exercise, training_day, sets, reps, weight, rest, created_at, updated_at)
VALUES (1, 1, 'LUNES', 4, 10, 70.00, 90, NOW(), NOW()),
       (1, 4, 'LUNES', 4, 12, 22.00, 60, NOW(), NOW()),
       (1, 6, 'LUNES', 3, 12, 0.00, 60, NOW(), NOW()),
       (2, 2, 'MARTES', 4, 8, 90.00, 120, NOW(), NOW()),
       (2, 9, 'MARTES', 4, 12, 160.00, 90, NOW(), NOW()),
       (3, 1, 'MIERCOLES', 4, 10, 75.00, 90, NOW(), NOW()),
       (3, 8, 'MIERCOLES', 4, 15, 25.00, 60, NOW(), NOW()),
       (4, 3, 'JUEVES', 3, 6, 120.00, 120, NOW(), NOW()),
       (4, 5, 'JUEVES', 4, 8, 0.00, 90, NOW(), NOW()),
       (4, 7, 'JUEVES', 3, 12, 30.00, 60, NOW(), NOW()),
       (5, 2, 'VIERNES', 3, 10, 70.00, 90, NOW(), NOW()),
       (5, 12, 'VIERNES', 4, 60, 0.00, 45, NOW(), NOW());

INSERT INTO workout_logs (id_member, id_routine_exercise, date, actual_sets, actual_reps, actual_weight, observation, created_at, updated_at)
VALUES ('c1111111-1111-1111-1111-111111111111', 1, '2025-05-01', 4, 10, 70.00, 'Técnica limpia con pausa en el pecho', NOW(), NOW()),
       ('c1111111-1111-1111-1111-111111111111', 2, '2025-05-01', 4, 12, 22.00, 'Buena activación del deltoides', NOW(), NOW()),
       ('c2222222-2222-2222-2222-222222222222', 4, '2025-05-02', 4, 8, 90.00, 'Profundidad completa alcanzada', NOW(), NOW()),
       ('c3333333-3333-3333-3333-333333333333', 6, '2025-05-03', 4, 10, 75.00, 'Excelente retracción escapular', NOW(), NOW()),
       ('c4444444-4444-4444-4444-444444444444', 8, '2025-05-04', 3, 6, 120.00, 'Carga manejada con bloqueo firme', NOW(), NOW()),
       ('c5555555-5555-5555-5555-555555555555', 11, '2025-05-05', 3, 10, 70.00, 'Movilidad mejorada en rodillas', NOW(), NOW());

INSERT INTO goals (name, description, created_at, updated_at)
VALUES ('Aumentar Masa Muscular', 'Incremento del volumen y tono muscular mediante sobrecarga progresiva', NOW(), NOW()),
       ('Pérdida de Grasa Corporal', 'Reducción del porcentaje graso corporal manteniendo masa magra', NOW(), NOW()),
       ('Mejora Cardiovascular', 'Incremento de la capacidad pulmonar y resistencia en pruebas aeróbicas', NOW(), NOW()),
       ('Fuerza 1RM Sentadilla', 'Superar marca de levantamiento en sentadilla profunda', NOW(), NOW()),
       ('Salud y Movilidad Articular', 'Disminución de molestias posturales y mayor rango de movimiento', NOW(), NOW());

INSERT INTO member_goals (id_member, id_goal, created_at, updated_at)
VALUES ('c1111111-1111-1111-1111-111111111111', 1, NOW(), NOW()),
       ('c2222222-2222-2222-2222-222222222222', 2, NOW(), NOW()),
       ('c3333333-3333-3333-3333-333333333333', 3, NOW(), NOW()),
       ('c4444444-4444-4444-4444-444444444444', 4, NOW(), NOW()),
       ('c5555555-5555-5555-5555-555555555555', 5, NOW(), NOW());

INSERT INTO physical_measurements (id_member, date, weight, height, body_fat_percentage, muscle_mass, waist, arm, leg, observations, created_at, updated_at)
VALUES ('c1111111-1111-1111-1111-111111111111', '2025-01-05', 76.50, 1.78, 22.00, 33.00, 85.00, 32.00, 54.00, 'Evaluación inicial del socio', NOW(), NOW()),
       ('c1111111-1111-1111-1111-111111111111', '2025-02-05', 75.20, 1.78, 20.50, 33.80, 83.50, 32.50, 54.50, 'Descenso sostenido en grasa visceral', NOW(), NOW()),
       ('c1111111-1111-1111-1111-111111111111', '2025-03-05', 74.00, 1.78, 19.00, 34.50, 81.50, 33.20, 55.00, 'Mayor tono y fuerza en tren superior', NOW(), NOW()),
       ('c1111111-1111-1111-1111-111111111111', '2025-04-05', 72.80, 1.78, 17.50, 35.10, 80.00, 33.80, 55.50, 'Definición abdominal visible', NOW(), NOW()),
       ('c1111111-1111-1111-1111-111111111111', '2025-05-05', 71.50, 1.78, 16.00, 35.80, 78.50, 34.50, 56.00, 'Cumplimiento óptimo de objetivos semestrales', NOW(), NOW()),
       ('c2222222-2222-2222-2222-222222222222', '2025-01-06', 84.00, 1.72, 26.00, 33.50, 93.00, 32.00, 57.00, 'Inicio de plan de descenso de peso', NOW(), NOW()),
       ('c2222222-2222-2222-2222-222222222222', '2025-03-06', 80.50, 1.72, 23.00, 34.20, 89.00, 33.00, 58.00, 'Progreso notable en resistencia y cintura', NOW(), NOW()),
       ('c3333333-3333-3333-3333-333333333333', '2025-01-07', 61.00, 1.65, 21.00, 26.00, 69.00, 25.50, 48.00, 'Evaluación postural y fuerza básica', NOW(), NOW()),
       ('c3333333-3333-3333-3333-333333333333', '2025-04-07', 62.50, 1.65, 19.50, 28.00, 67.50, 26.50, 50.00, 'Incremento en masa muscular magra', NOW(), NOW());

INSERT INTO payment_methods (name, description, created_at, updated_at)
VALUES ('Tarjeta de Débito', 'Cobro electrónico con tarjeta bancaria de débito', NOW(), NOW()),
       ('Tarjeta de Crédito', 'Cobro con tarjeta de crédito bancaria', NOW(), NOW()),
       ('Transferencia Bancaria', 'Depósito o transferencia bancaria electrónica', NOW(), NOW()),
       ('Efectivo', 'Pago en efectivo en caja de recepción de la sucursal', NOW(), NOW());

INSERT INTO invoices (id_branch, id_membership, id_receptionist, status, id_promotion, amount, issue_date, due_date, observation, created_at, updated_at)
VALUES (1, 1, 'b1111111-1111-1111-1111-111111111111', 'PAID', NULL, 250.00, '2025-01-01', '2025-01-05', 'Factura membresía básica mensual', NOW(), NOW()),
       (2, 2, 'b2222222-2222-2222-2222-222222222222', 'PAID', NULL, 450.00, '2025-01-01', '2025-01-05', 'Factura membresía fitness total', NOW(), NOW()),
       (3, 3, 'b3333333-3333-3333-3333-333333333333', 'PAID', 1, 935.00, '2025-01-01', '2025-01-05', 'Factura plan trimestral con 15% descuento', NOW(), NOW()),
       (4, 4, 'b4444444-4444-4444-4444-444444444444', 'PARTIALLY_PAID', 2, 3040.00, '2025-01-01', '2025-01-15', 'Factura anual VIP con abono parcial', NOW(), NOW()),
       (1, 5, 'b1111111-1111-1111-1111-111111111111', 'PENDING', NULL, 450.00, '2025-02-01', '2025-02-08', 'Cobro mensualidad pendiente', NOW(), NOW());

INSERT INTO payment_receipts (id_invoice, id_payment_method, payment_date, amount_paid, reference, created_at, updated_at)
VALUES (1, 1, NOW(), 250.00, 'REC-GT-0001', NOW(), NOW()),
       (2, 2, NOW(), 450.00, 'REC-GT-0002', NOW(), NOW()),
       (3, 3, NOW(), 935.00, 'REC-GT-0003', NOW(), NOW()),
       (4, 1, NOW(), 1500.00, 'REC-GT-0004-ABONO', NOW(), NOW());

INSERT INTO user_tokens (id_user, token, type, generation_date, expiration_date, used, created_at, updated_at)
VALUES ('c1111111-1111-1111-1111-111111111111', 'tok_verify_c1111111_seed_token', 'EMAIL_VERIFICATION', NOW(), NOW() + INTERVAL '24 HOURS', TRUE, NOW(), NOW()),
       ('c2222222-2222-2222-2222-222222222222', 'tok_verify_c2222222_seed_token', 'EMAIL_VERIFICATION', NOW(), NOW() + INTERVAL '24 HOURS', TRUE, NOW(), NOW()),
       ('c3333333-3333-3333-3333-333333333333', 'tok_reset_c3333333_seed_token', 'PASSWORD_RESET', NOW(), NOW() + INTERVAL '2 HOURS', FALSE, NOW(), NOW()),
       ('c4444444-4444-4444-4444-444444444444', 'tok_session_c4444444_seed_token', 'SESSION_AUTH', NOW(), NOW() + INTERVAL '30 DAYS', FALSE, NOW(), NOW());

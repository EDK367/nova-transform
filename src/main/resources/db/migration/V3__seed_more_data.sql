
INSERT INTO branches (name, address, phone, active, created_at, updated_at)
VALUES ('Sede Central', 'Av. Principal 100, Centro', '+52 555 100 0001', TRUE, NOW(), NOW()),
       ('Sede Norte', 'Blvd. Norte 250, Plaza Norte', '+52 555 100 0002', TRUE, NOW(), NOW()),
       ('Sede Sur', 'Calz. del Sur 450, Sur', '+52 555 100 0003', TRUE, NOW(), NOW()),
       ('Sede Poniente', 'Av. Poniente 800, Plaza Sol', '+52 555 100 0004', TRUE, NOW(), NOW());

INSERT INTO users (id, id_role, id_branch, first_name, last_name, email, phone, username, password, active, two_factor_auth, created_at, updated_at)
VALUES ('a1111111-1111-1111-1111-111111111111', (SELECT id FROM roles WHERE name = 'ENTRENADOR'), 1, 'Alejandro', 'Vargas', 'alejandro.vargas@nova.com', '+52 555 200 0001', 'trainer1', '$2a$10$PYPvsjogJjjAj4amIibEoObCM5A20lP30BFM7DoDYV.04X9l4Q1oe', TRUE, FALSE, NOW(), NOW()),
       ('a2222222-2222-2222-2222-222222222222', (SELECT id FROM roles WHERE name = 'ENTRENADOR'), 2, 'Beatriz', 'Morales', 'beatriz.morales@nova.com', '+52 555 200 0002', 'trainer2', '$2a$10$PYPvsjogJjjAj4amIibEoObCM5A20lP30BFM7DoDYV.04X9l4Q1oe', TRUE, FALSE, NOW(), NOW()),
       ('a3333333-3333-3333-3333-333333333333', (SELECT id FROM roles WHERE name = 'ENTRENADOR'), 3, 'Carlos', 'Guerrero', 'carlos.guerrero@nova.com', '+52 555 200 0003', 'trainer3', '$2a$10$PYPvsjogJjjAj4amIibEoObCM5A20lP30BFM7DoDYV.04X9l4Q1oe', TRUE, FALSE, NOW(), NOW()),
       ('a4444444-4444-4444-4444-444444444444', (SELECT id FROM roles WHERE name = 'ENTRENADOR'), 4, 'Diana', 'Castro', 'diana.castro@nova.com', '+52 555 200 0004', 'trainer4', '$2a$10$PYPvsjogJjjAj4amIibEoObCM5A20lP30BFM7DoDYV.04X9l4Q1oe', TRUE, FALSE, NOW(), NOW()),
       ('b1111111-1111-1111-1111-111111111111', (SELECT id FROM roles WHERE name = 'RECEPCIONISTA'), 1, 'Elena', 'Torres', 'elena.torres@nova.com', '+52 555 300 0001', 'recep1', '$2a$10$PYPvsjogJjjAj4amIibEoObCM5A20lP30BFM7DoDYV.04X9l4Q1oe', TRUE, FALSE, NOW(), NOW()),
       ('b2222222-2222-2222-2222-222222222222', (SELECT id FROM roles WHERE name = 'RECEPCIONISTA'), 2, 'Fernando', 'Luna', 'fernando.luna@nova.com', '+52 555 300 0002', 'recep2', '$2a$10$PYPvsjogJjjAj4amIibEoObCM5A20lP30BFM7DoDYV.04X9l4Q1oe', TRUE, FALSE, NOW(), NOW()),
       ('b3333333-3333-3333-3333-333333333333', (SELECT id FROM roles WHERE name = 'RECEPCIONISTA'), 3, 'Gabriela', 'Rios', 'gabriela.rios@nova.com', '+52 555 300 0003', 'recep3', '$2a$10$PYPvsjogJjjAj4amIibEoObCM5A20lP30BFM7DoDYV.04X9l4Q1oe', TRUE, FALSE, NOW(), NOW()),
       ('b4444444-4444-4444-4444-444444444444', (SELECT id FROM roles WHERE name = 'RECEPCIONISTA'), 4, 'Hugo', 'Paredes', 'hugo.paredes@nova.com', '+52 555 300 0004', 'recep4', '$2a$10$PYPvsjogJjjAj4amIibEoObCM5A20lP30BFM7DoDYV.04X9l4Q1oe', TRUE, FALSE, NOW(), NOW()),
       ('c1111111-1111-1111-1111-111111111111', (SELECT id FROM roles WHERE name = 'CLIENTE'), 1, 'Isabel', 'Navarro', 'isabel.navarro@nova.com', '+52 555 400 0001', 'member1', '$2a$10$PYPvsjogJjjAj4amIibEoObCM5A20lP30BFM7DoDYV.04X9l4Q1oe', TRUE, FALSE, NOW(), NOW()),
       ('c2222222-2222-2222-2222-222222222222', (SELECT id FROM roles WHERE name = 'CLIENTE'), 2, 'Jorge', 'Serrano', 'jorge.serrano@nova.com', '+52 555 400 0002', 'member2', '$2a$10$PYPvsjogJjjAj4amIibEoObCM5A20lP30BFM7DoDYV.04X9l4Q1oe', TRUE, FALSE, NOW(), NOW()),
       ('c3333333-3333-3333-3333-333333333333', (SELECT id FROM roles WHERE name = 'CLIENTE'), 3, 'Karla', 'Mendoza', 'karla.mendoza@nova.com', '+52 555 400 0003', 'member3', '$2a$10$PYPvsjogJjjAj4amIibEoObCM5A20lP30BFM7DoDYV.04X9l4Q1oe', TRUE, FALSE, NOW(), NOW()),
       ('c4444444-4444-4444-4444-444444444444', (SELECT id FROM roles WHERE name = 'CLIENTE'), 4, 'Luis', 'Ortega', 'luis.ortega@nova.com', '+52 555 400 0004', 'member4', '$2a$10$PYPvsjogJjjAj4amIibEoObCM5A20lP30BFM7DoDYV.04X9l4Q1oe', TRUE, FALSE, NOW(), NOW());

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
       ('c4444444-4444-4444-4444-444444444444', '2024-05-15', NOW(), NOW());

INSERT INTO benefits (name, description, created_at, updated_at)
VALUES ('Acceso Área de Pesas', 'Uso ilimitado del área de pesas libres y máquinas integradas', NOW(), NOW()),
       ('Clases Grupales', 'Acceso a todas las clases grupales programadas en la semana', NOW(), NOW()),
       ('Sauna y Vapor', 'Uso de las instalaciones húmedas de relajación', NOW(), NOW()),
       ('Acceso 24/7', 'Entrada sin restricción de horario a las sucursales participantes', NOW(), NOW());

INSERT INTO membership_plans (name, duration, price, description, created_at, updated_at)
VALUES ('Plan Básico', 30, 499.00, 'Acceso básico mensual para entrenamiento individual', NOW(), NOW()),
       ('Plan Fitness', 30, 799.00, 'Acceso mensual con clases grupales incluidas', NOW(), NOW()),
       ('Plan Trimestral Pro', 90, 1999.00, 'Acceso completo trimestral con sauna incluido', NOW(), NOW()),
       ('Plan Anual VIP', 365, 6999.00, 'Membresía anual con todos los beneficios y acceso 24/7', NOW(), NOW());

INSERT INTO benefit_plans (id_plan, id_benefit, created_at, updated_at)
VALUES (1, 1, NOW(), NOW()),
       (2, 2, NOW(), NOW()),
       (3, 3, NOW(), NOW()),
       (4, 4, NOW(), NOW());

INSERT INTO plan_price_histories (id_plan, price, start_date, end_date, observation, created_at, updated_at)
VALUES (1, 450.00, '2023-01-01', '2023-12-31', 'Tarifa inicial', NOW(), NOW()),
       (2, 750.00, '2023-01-01', '2023-12-31', 'Tarifa anterior', NOW(), NOW()),
       (3, 1850.00, '2023-01-01', '2023-12-31', 'Promoción de apertura', NOW(), NOW()),
       (4, 6499.00, '2023-01-01', '2023-12-31', 'Precio de lanzamiento', NOW(), NOW());

INSERT INTO promotions (name, description, discount_type, discount_value, start_date, end_date, active, created_at, updated_at)
VALUES ('Promo Verano', '15% de descuento en planes trimestrales', 'PERCENTAGE', 15.00, '2025-06-01', '2025-08-31', TRUE, NOW(), NOW()),
       ('Promo Año Nuevo', '20% de descuento en plan anual', 'PERCENTAGE', 20.00, '2025-01-01', '2025-01-31', TRUE, NOW(), NOW()),
       ('Bono Amigo', 'Descuento directo de 100 pesos', 'FIXED_AMOUNT', 100.00, '2025-01-01', '2025-12-31', TRUE, NOW(), NOW()),
       ('Promo Estudiantes', '10% de descuento permanente', 'PERCENTAGE', 10.00, '2025-01-01', '2025-12-31', TRUE, NOW(), NOW());

INSERT INTO memberships (id_member, id_plan, status, start_date, due_date, created_at, updated_at)
VALUES ('c1111111-1111-1111-1111-111111111111', 1, 'ACTIVE', '2025-01-01', '2025-01-31', NOW(), NOW()),
       ('c2222222-2222-2222-2222-222222222222', 2, 'ACTIVE', '2025-01-01', '2025-01-31', NOW(), NOW()),
       ('c3333333-3333-3333-3333-333333333333', 3, 'ACTIVE', '2025-01-01', '2025-03-31', NOW(), NOW()),
       ('c4444444-4444-4444-4444-444444444444', 4, 'ACTIVE', '2025-01-01', '2025-12-31', NOW(), NOW());

INSERT INTO membership_histories (id_membership, previous_status, new_status, change_date, observation, created_at, updated_at)
VALUES (1, 'INACTIVE', 'ACTIVE', NOW(), 'Activación inicial de membresía', NOW(), NOW()),
       (2, 'INACTIVE', 'ACTIVE', NOW(), 'Activación inicial de membresía', NOW(), NOW()),
       (3, 'INACTIVE', 'ACTIVE', NOW(), 'Activación inicial de membresía', NOW(), NOW()),
       (4, 'INACTIVE', 'ACTIVE', NOW(), 'Activación inicial de membresía', NOW(), NOW());

INSERT INTO membership_freezes (id_membership, start_date, end_date, reason, created_at, updated_at)
VALUES (1, '2025-02-01', '2025-02-15', 'Vacaciones laborales', NOW(), NOW()),
       (2, '2025-02-10', '2025-02-24', 'Tratamiento médico menor', NOW(), NOW()),
       (3, '2025-03-01', '2025-03-15', 'Viaje de trabajo', NOW(), NOW()),
       (4, '2025-04-01', '2025-04-14', 'Periodo de exámenes', NOW(), NOW());

INSERT INTO class_types (name, description, created_at, updated_at)
VALUES ('Spinning', 'Entrenamiento cardiovascular intensivo sobre bicicleta estacionaria', NOW(), NOW()),
       ('Crossfit', 'Entrenamiento funcional de alta intensidad con ejercicios variados', NOW(), NOW()),
       ('Yoga Vinyasa', 'Secuencias fluidas de posturas coordinadas con la respiración', NOW(), NOW()),
       ('Pilates Mat', 'Ejercicios en colchoneta enfocados en fuerza del core y estabilidad', NOW(), NOW());

INSERT INTO classes (id_branch, id_class_type, id_trainer, status, schedule, duration, max_capacity, created_at, updated_at)
VALUES (1, 1, 'a1111111-1111-1111-1111-111111111111', 'SCHEDULED', '2025-06-01 07:00:00', 60, 20, NOW(), NOW()),
       (2, 2, 'a2222222-2222-2222-2222-222222222222', 'SCHEDULED', '2025-06-01 08:30:00', 60, 15, NOW(), NOW()),
       (3, 3, 'a3333333-3333-3333-3333-333333333333', 'SCHEDULED', '2025-06-01 18:00:00', 60, 25, NOW(), NOW()),
       (4, 4, 'a4444444-4444-4444-4444-444444444444', 'SCHEDULED', '2025-06-01 19:30:00', 60, 15, NOW(), NOW());

INSERT INTO class_enrollments (id_class, id_member, status, enrollment_date, created_at, updated_at)
VALUES (1, 'c1111111-1111-1111-1111-111111111111', 'ENROLLED', NOW(), NOW(), NOW()),
       (2, 'c2222222-2222-2222-2222-222222222222', 'ENROLLED', NOW(), NOW(), NOW()),
       (3, 'c3333333-3333-3333-3333-333333333333', 'ENROLLED', NOW(), NOW(), NOW()),
       (4, 'c4444444-4444-4444-4444-444444444444', 'ENROLLED', NOW(), NOW(), NOW());

INSERT INTO class_ratings (id_enrollment, rating, comment, date_time, created_at, updated_at)
VALUES (1, 5, 'Excelente instructor y gran energía en la clase de spinning', NOW(), NOW(), NOW()),
       (2, 5, 'Entrenamiento exigente y bien guiado', NOW(), NOW(), NOW()),
       (3, 4, 'Muy buena clase de yoga, relajante y dinámica', NOW(), NOW(), NOW()),
       (4, 5, 'Rutina de pilates muy bien explicada para todos los niveles', NOW(), NOW(), NOW());

INSERT INTO attendances (id_branch, id_receptionist, id_member, date, entry_time, exit_time, created_at, updated_at)
VALUES (1, 'b1111111-1111-1111-1111-111111111111', 'c1111111-1111-1111-1111-111111111111', '2025-05-02', '07:05:00', '08:30:00', NOW(), NOW()),
       (2, 'b2222222-2222-2222-2222-222222222222', 'c2222222-2222-2222-2222-222222222222', '2025-05-06', '08:00:00', '09:45:00', NOW(), NOW()),
       (3, 'b3333333-3333-3333-3333-333333333333', 'c3333333-3333-3333-3333-333333333333', '2025-05-11', '18:15:00', '19:30:00', NOW(), NOW()),
       (4, 'b4444444-4444-4444-4444-444444444444', 'c4444444-4444-4444-4444-444444444444', '2025-05-16', '19:00:00', '20:15:00', NOW(), NOW());

INSERT INTO waitlists (id_class, id_member, registration_date, notified, created_at, updated_at)
VALUES (1, 'c2222222-2222-2222-2222-222222222222', NOW(), FALSE, NOW(), NOW()),
       (2, 'c3333333-3333-3333-3333-333333333333', NOW(), FALSE, NOW(), NOW()),
       (3, 'c4444444-4444-4444-4444-444444444444', NOW(), FALSE, NOW(), NOW()),
       (4, 'c1111111-1111-1111-1111-111111111111', NOW(), FALSE, NOW(), NOW());

INSERT INTO specializations (name, description, created_at, updated_at)
VALUES ('Hipertrofia Muscular', 'Especialización en desarrollo y ganancia de masa muscular', NOW(), NOW()),
       ('Pérdida de Grasa', 'Enfoque en déficit energético y entrenamiento metabólico', NOW(), NOW()),
       ('Rehabilitación Deportiva', 'Recuperación y readaptación física post lesiones', NOW(), NOW()),
       ('Calistenia y Fuerza', 'Control corporal y ejercicios gimnásticos con peso propio', NOW(), NOW());

INSERT INTO trainer_specializations (id_trainer, id_specialization, created_at, updated_at)
VALUES ('a1111111-1111-1111-1111-111111111111', 1, NOW(), NOW()),
       ('a2222222-2222-2222-2222-222222222222', 2, NOW(), NOW()),
       ('a3333333-3333-3333-3333-333333333333', 3, NOW(), NOW()),
       ('a4444444-4444-4444-4444-444444444444', 4, NOW(), NOW());

INSERT INTO trainer_assignments (id_member, id_trainer, start_date, end_date, active, reason, created_at, updated_at)
VALUES ('c1111111-1111-1111-1111-111111111111', 'a1111111-1111-1111-1111-111111111111', '2025-01-05', NULL, TRUE, 'Asignación para plan de masa muscular', NOW(), NOW()),
       ('c2222222-2222-2222-2222-222222222222', 'a2222222-2222-2222-2222-222222222222', '2025-01-05', NULL, TRUE, 'Asignación para acondicionamiento metabólico', NOW(), NOW()),
       ('c3333333-3333-3333-3333-333333333333', 'a3333333-3333-3333-3333-333333333333', '2025-01-05', NULL, TRUE, 'Asignación para rehabilitación postural', NOW(), NOW()),
       ('c4444444-4444-4444-4444-444444444444', 'a4444444-4444-4444-4444-444444444444', '2025-01-05', NULL, TRUE, 'Asignación para entrenamiento de fuerza', NOW(), NOW());

INSERT INTO exercises (name, description, muscle_group, created_at, updated_at)
VALUES ('Press de Banca Plano', 'Empuje horizontal con barra para pectoral mayor', 'Pecho', NOW(), NOW()),
       ('Sentadilla Trasera', 'Flexión de rodillas y cadera con barra para cuádriceps y glúteos', 'Pierna', NOW(), NOW()),
       ('Peso Muerto Convencional', 'Tracción vertical desde el suelo para cadena posterior', 'Espalda', NOW(), NOW()),
       ('Press Militar con Mancuernas', 'Empuje vertical para deltoides', 'Hombro', NOW(), NOW());

INSERT INTO routines (id_assignment, name, description, created_at, updated_at, active)
VALUES (1, 'Rutina Hipertrofia Pectoral', 'Rutina orientada al volumen e intensidad de pecho', NOW(), NOW(), TRUE),
       (2, 'Rutina Acondicionamiento', 'Circuito de fuerza y resistencia cardiovascular', NOW(), NOW(), TRUE),
       (3, 'Rutina Movilidad y Espalda', 'Ejercicios correctivos de espalda y postura', NOW(), NOW(), TRUE),
       (4, 'Rutina Fuerza Básicos', 'Enfocada en progresión en sentadilla y peso muerto', NOW(), NOW(), TRUE);

INSERT INTO routine_exercises (id_routine, id_exercise, training_day, sets, reps, weight, rest, created_at, updated_at)
VALUES (1, 1, 'LUNES', 4, 10, 70.00, 90, NOW(), NOW()),
       (2, 2, 'MARTES', 4, 12, 80.00, 90, NOW(), NOW()),
       (3, 3, 'MIERCOLES', 3, 8, 100.00, 120, NOW(), NOW()),
       (4, 4, 'JUEVES', 4, 10, 20.00, 60, NOW(), NOW());

INSERT INTO workout_logs (id_member, id_routine_exercise, date, actual_sets, actual_reps, actual_weight, observation, created_at, updated_at)
VALUES ('c1111111-1111-1111-1111-111111111111', 1, NOW(), 4, 10, 70.00, 'Buena técnica completada con éxito', NOW(), NOW()),
       ('c2222222-2222-2222-2222-222222222222', 2, NOW(), 4, 12, 80.00, 'Completadas todas las repeticiones', NOW(), NOW()),
       ('c3333333-3333-3333-3333-333333333333', 3, NOW(), 3, 8, 100.00, 'Se mantuvo buena postura lumbar', NOW(), NOW()),
       ('c4444444-4444-4444-4444-444444444444', 4, NOW(), 4, 10, 20.00, 'Buena estabilidad escapular', NOW(), NOW());

INSERT INTO goals (name, description, created_at, updated_at)
VALUES ('Aumentar Masa Muscular', 'Incremento del volumen y tono muscular mediante fuerza', NOW(), NOW()),
       ('Pérdida de Peso', 'Reducción del porcentaje graso corporal', NOW(), NOW()),
       ('Mejora Cardiovascular', 'Incremento de la capacidad pulmonar y resistencia', NOW(), NOW()),
       ('Salud y Movilidad', 'Disminución de dolores posturales y aumento de flexibilidad', NOW(), NOW());

INSERT INTO member_goals (id_member, id_goal, created_at, updated_at)
VALUES ('c1111111-1111-1111-1111-111111111111', 1, NOW(), NOW()),
       ('c2222222-2222-2222-2222-222222222222', 2, NOW(), NOW()),
       ('c3333333-3333-3333-3333-333333333333', 3, NOW(), NOW()),
       ('c4444444-4444-4444-4444-444444444444', 4, NOW(), NOW());

INSERT INTO physical_measurements (id_member, date, weight, height, body_fat_percentage, muscle_mass, waist, arm, leg, observations, created_at, updated_at)
VALUES ('c1111111-1111-1111-1111-111111111111', '2025-01-05', 75.50, 1.78, 15.20, 38.50, 82.00, 35.00, 56.00, 'Evaluación inicial en buen estado físico', NOW(), NOW()),
       ('c2222222-2222-2222-2222-222222222222', '2025-01-06', 82.00, 1.72, 24.50, 34.00, 91.00, 33.00, 58.00, 'Meta de reducción de grasa abdominal', NOW(), NOW()),
       ('c3333333-3333-3333-3333-333333333333', '2025-01-07', 60.00, 1.65, 20.00, 26.50, 68.00, 26.00, 48.00, 'Enfoque en fuerza general y postura', NOW(), NOW()),
       ('c4444444-4444-4444-4444-444444444444', '2025-01-08', 90.00, 1.85, 18.00, 44.00, 88.00, 39.00, 62.00, 'Excelente nivel de fuerza base', NOW(), NOW());

INSERT INTO payment_methods (name, description, created_at, updated_at)
VALUES ('Tarjeta de Débito', 'Cobro electrónico con tarjeta bancaria de débito', NOW(), NOW()),
       ('Tarjeta de Crédito', 'Cobro con tarjeta de crédito bancaria', NOW(), NOW()),
       ('Transferencia Bancaria', 'Depósito o transferencia SPEI a cuenta de la sucursal', NOW(), NOW()),
       ('Efectivo', 'Pago en moneda de curso legal en caja de recepción', NOW(), NOW());

INSERT INTO invoices (id_branch, id_membership, id_receptionist, status, id_promotion, amount, issue_date, due_date, observation, created_at, updated_at)
VALUES (1, 1, 'b1111111-1111-1111-1111-111111111111', 'PAID', NULL, 499.00, '2025-01-01', '2025-01-05', 'Factura membresía básica', NOW(), NOW()),
       (2, 2, 'b2222222-2222-2222-2222-222222222222', 'PAID', NULL, 799.00, '2025-01-01', '2025-01-05', 'Factura membresía fitness', NOW(), NOW()),
       (3, 3, 'b3333333-3333-3333-3333-333333333333', 'PAID', 1, 1699.15, '2025-01-01', '2025-01-05', 'Factura con descuento de verano', NOW(), NOW()),
       (4, 4, 'b4444444-4444-4444-4444-444444444444', 'PAID', 2, 5599.20, '2025-01-01', '2025-01-05', 'Factura plan anual promo', NOW(), NOW());

INSERT INTO payment_receipts (id_invoice, id_payment_method, payment_date, amount_paid, reference, created_at, updated_at)
VALUES (1, 1, NOW(), 499.00, 'REC-2025-0001', NOW(), NOW()),
       (2, 2, NOW(), 799.00, 'REC-2025-0002', NOW(), NOW()),
       (3, 3, NOW(), 1699.15, 'REC-2025-0003', NOW(), NOW()),
       (4, 4, NOW(), 5599.20, 'REC-2025-0004', NOW(), NOW());

INSERT INTO user_tokens (id_user, token, type, generation_date, expiration_date, used, created_at, updated_at)
VALUES ('c1111111-1111-1111-1111-111111111111', 'tok_verify_c1111111_seed_token', 'EMAIL_VERIFICATION', NOW(), NOW() + INTERVAL '24 HOURS', TRUE, NOW(), NOW()),
       ('c2222222-2222-2222-2222-222222222222', 'tok_verify_c2222222_seed_token', 'EMAIL_VERIFICATION', NOW(), NOW() + INTERVAL '24 HOURS', TRUE, NOW(), NOW()),
       ('c3333333-3333-3333-3333-333333333333', 'tok_reset_c3333333_seed_token', 'PASSWORD_RESET', NOW(), NOW() + INTERVAL '2 HOURS', FALSE, NOW(), NOW()),
       ('c4444444-4444-4444-4444-444444444444', 'tok_session_c4444444_seed_token', 'SESSION_AUTH', NOW(), NOW() + INTERVAL '30 DAYS', FALSE, NOW(), NOW());

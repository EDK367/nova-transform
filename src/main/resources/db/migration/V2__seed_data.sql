-- ============================================================
-- ROLES
-- ============================================================
INSERT INTO roles (name, description, is_system, created_at, updated_at)
VALUES ('SUPER_ADMIN', 'Propietario y Superusuario del Sistema (Acceso Total y Gestión de Roles/Permisos)', TRUE, NOW(), NOW()),
       ('ADMIN', 'Administrador del Negocio (Operaciones, Sucursales, Personal y Finanzas)', TRUE, NOW(), NOW()),
       ('ENTRENADOR', 'Entrenador e Instructor de Clases y Rutinas', TRUE, NOW(), NOW()),
       ('RECEPCIONISTA', 'Recepcionista (Atención a Clientes, Asistencias y Pagos)', TRUE, NOW(), NOW()),
       ('CLIENTE', 'Cliente del Gimnasio', TRUE, NOW(), NOW());

-- ============================================================
-- PERMISSIONS CATALOG
-- ============================================================
INSERT INTO permissions (code, name, module, description, created_at, updated_at)
VALUES
    -- SEGURIDAD
    ('ROLES_READ', 'Consultar Roles', 'SEGURIDAD', 'Permite visualizar el catálogo de roles y sus permisos asignados', NOW(), NOW()),
    ('ROLES_MANAGE', 'Administrar Roles', 'SEGURIDAD', 'Permite crear, actualizar, eliminar roles y modificar sus permisos', NOW(), NOW()),
    ('PERMISSIONS_READ', 'Consultar Permisos', 'SEGURIDAD', 'Permite visualizar el catálogo de permisos del sistema', NOW(), NOW()),
    ('PERMISSIONS_MANAGE', 'Administrar Permisos', 'SEGURIDAD', 'Permite crear, editar y eliminar permisos del catálogo', NOW(), NOW()),

    -- USUARIOS Y PERSONAL
    ('USERS_READ', 'Consultar Usuarios', 'USUARIOS', 'Permite ver el directorio de cuentas de usuarios internos', NOW(), NOW()),
    ('USERS_CREATE', 'Crear Usuarios', 'USUARIOS', 'Permite registrar nuevas cuentas de personal en el sistema', NOW(), NOW()),
    ('USERS_UPDATE', 'Actualizar Usuarios', 'USUARIOS', 'Permite editar datos, sucursal, rol y estado de usuarios', NOW(), NOW()),
    ('USERS_DELETE', 'Desactivar/Eliminar Usuarios', 'USUARIOS', 'Permite dar de baja cuentas de usuario del sistema', NOW(), NOW()),
    ('RECEPTIONISTS_MANAGE', 'Gestionar Recepcionistas', 'USUARIOS', 'Permite administrar perfiles y turnos de recepcionistas', NOW(), NOW()),

    -- SUCURSALES
    ('BRANCHES_READ', 'Consultar Sucursales', 'SUCURSALES', 'Permite visualizar las sedes y sucursales del gimnasio', NOW(), NOW()),
    ('BRANCHES_MANAGE', 'Administrar Sucursales', 'SUCURSALES', 'Permite crear, editar y activar/desactivar sucursales', NOW(), NOW()),

    -- CLIENTES Y SALUD
    ('MEMBERS_READ', 'Consultar Clientes', 'CLIENTES', 'Permite consultar el directorio y perfil de clientes', NOW(), NOW()),
    ('MEMBERS_CREATE', 'Registrar Clientes', 'CLIENTES', 'Permite inscribir nuevos clientes en el gimnasio', NOW(), NOW()),
    ('MEMBERS_UPDATE', 'Actualizar Clientes', 'CLIENTES', 'Permite modificar datos de contacto de los clientes', NOW(), NOW()),
    ('MEMBERS_DELETE', 'Dar de Baja Clientes', 'CLIENTES', 'Permite desactivar o eliminar registros de clientes', NOW(), NOW()),
    ('GOALS_READ', 'Consultar Metas', 'CLIENTES', 'Permite visualizar metas y objetivos físicos', NOW(), NOW()),
    ('GOALS_MANAGE', 'Gestionar Metas', 'CLIENTES', 'Permite crear, asignar y actualizar metas de clientes', NOW(), NOW()),
    ('MEASUREMENTS_READ', 'Consultar Medidas Físicas', 'CLIENTES', 'Permite visualizar el historial antropométrico', NOW(), NOW()),
    ('MEASUREMENTS_MANAGE', 'Registrar Medidas Físicas', 'CLIENTES', 'Permite registrar y actualizar evaluaciones físicas', NOW(), NOW()),

    -- MEMBRESÍAS, PLANES Y BENEFICIOS
    ('MEMBERSHIPS_READ', 'Consultar Membresías', 'MEMBRESIAS', 'Permite visualizar membresías vigentes e historial', NOW(), NOW()),
    ('MEMBERSHIPS_MANAGE', 'Gestionar Membresías', 'MEMBRESIAS', 'Permite asignar, cambiar de plan, congelar o cancelar membresías', NOW(), NOW()),
    ('PLANS_READ', 'Consultar Planes', 'MEMBRESIAS', 'Permite visualizar los planes de membresía y precios', NOW(), NOW()),
    ('PLANS_MANAGE', 'Administrar Planes', 'MEMBRESIAS', 'Permite crear, actualizar y gestionar planes de membresía', NOW(), NOW()),
    ('BENEFITS_READ', 'Consultar Beneficios', 'MEMBRESIAS', 'Permite visualizar los beneficios incluidos en cada plan', NOW(), NOW()),
    ('BENEFITS_MANAGE', 'Administrar Beneficios', 'MEMBRESIAS', 'Permite crear, editar y vincular beneficios a planes', NOW(), NOW()),

    -- ENTRENAMIENTO Y RUTINAS
    ('TRAINERS_READ', 'Consultar Entrenadores', 'ENTRENAMIENTO', 'Permite visualizar el directorio de entrenadores', NOW(), NOW()),
    ('TRAINERS_MANAGE', 'Administrar Entrenadores', 'ENTRENAMIENTO', 'Permite gestionar perfiles de entrenadores', NOW(), NOW()),
    ('SPECIALIZATIONS_READ', 'Consultar Especialidades', 'ENTRENAMIENTO', 'Permite ver especialidades deportivas', NOW(), NOW()),
    ('SPECIALIZATIONS_MANAGE', 'Gestionar Especialidades', 'ENTRENAMIENTO', 'Permite crear, editar y asignar especialidades a entrenadores', NOW(), NOW()),
    ('ASSIGNMENTS_READ', 'Consultar Asignaciones', 'ENTRENAMIENTO', 'Permite ver asignaciones entrenador-cliente', NOW(), NOW()),
    ('ASSIGNMENTS_MANAGE', 'Gestionar Asignaciones', 'ENTRENAMIENTO', 'Permite vincular y actualizar entrenadores asignados a clientes', NOW(), NOW()),
    ('EXERCISES_READ', 'Consultar Ejercicios', 'ENTRENAMIENTO', 'Permite ver el catálogo de ejercicios', NOW(), NOW()),
    ('EXERCISES_MANAGE', 'Gestionar Ejercicios', 'ENTRENAMIENTO', 'Permite crear y actualizar ejercicios del catálogo', NOW(), NOW()),
    ('ROUTINES_READ', 'Consultar Rutinas', 'ENTRENAMIENTO', 'Permite visualizar rutinas de entrenamiento', NOW(), NOW()),
    ('ROUTINES_MANAGE', 'Gestionar Rutinas', 'ENTRENAMIENTO', 'Permite diseñar, editar y asignar ejercicios a rutinas', NOW(), NOW()),
    ('WORKOUT_LOGS_READ', 'Consultar Bitácora', 'ENTRENAMIENTO', 'Permite revisar registros de entrenamiento', NOW(), NOW()),
    ('WORKOUT_LOGS_MANAGE', 'Registrar Bitácora', 'ENTRENAMIENTO', 'Permite registrar series, repeticiones y pesos en bitácora', NOW(), NOW()),

    -- CLASES Y ACCESOS
    ('CLASSES_READ', 'Consultar Clases', 'CLASES', 'Permite ver horarios y disponibilidad de clases grupales', NOW(), NOW()),
    ('CLASSES_MANAGE', 'Programar Clases', 'CLASES', 'Permite crear, reprogramar y cancelar sesiones de clases', NOW(), NOW()),
    ('CLASS_TYPES_READ', 'Consultar Tipos de Clase', 'CLASES', 'Permite visualizar disciplinas de clases grupales', NOW(), NOW()),
    ('CLASS_TYPES_MANAGE', 'Gestionar Tipos de Clase', 'CLASES', 'Permite crear y actualizar disciplinas de clases', NOW(), NOW()),
    ('ENROLLMENTS_MANAGE', 'Inscribir a Clases', 'CLASES', 'Permite inscribir y cancelar lugares en clases grupales', NOW(), NOW()),
    ('ATTENDANCES_READ', 'Consultar Asistencias', 'CLASES', 'Permite ver el registro de entradas y salidas', NOW(), NOW()),
    ('ATTENDANCES_MANAGE', 'Registrar Check-in/Check-out', 'CLASES', 'Permite registrar ingresos y salidas de clientes en recepción', NOW(), NOW()),
    ('WAITLISTS_MANAGE', 'Gestionar Listas de Espera', 'CLASES', 'Permite anotarse y administrar listas de espera de clases', NOW(), NOW()),
    ('RATINGS_READ', 'Consultar Calificaciones', 'CLASES', 'Permite ver evaluaciones y comentarios de clases', NOW(), NOW()),
    ('RATINGS_CREATE', 'Calificar Clases', 'CLASES', 'Permite emitir calificaciones y reseñas de sesiones', NOW(), NOW()),

    -- FINANZAS
    ('INVOICES_READ', 'Consultar Facturas', 'FINANZAS', 'Permite visualizar facturas y estados de cuenta', NOW(), NOW()),
    ('INVOICES_MANAGE', 'Emitir y Gestionar Facturas', 'FINANZAS', 'Permite generar, actualizar y anular facturas', NOW(), NOW()),
    ('PAYMENTS_READ', 'Consultar Comprobantes', 'FINANZAS', 'Permite visualizar recibos y comprobantes de pago', NOW(), NOW()),
    ('PAYMENTS_MANAGE', 'Registrar Cobros y Pagos', 'FINANZAS', 'Permite emitir comprobantes de pago sobre facturas', NOW(), NOW()),
    ('PAYMENT_METHODS_READ', 'Consultar Métodos de Pago', 'FINANZAS', 'Permite ver formas de pago aceptadas', NOW(), NOW()),
    ('PAYMENT_METHODS_MANAGE', 'Administrar Métodos de Pago', 'FINANZAS', 'Permite crear y editar métodos de pago', NOW(), NOW()),
    ('PROMOTIONS_READ', 'Consultar Promociones', 'FINANZAS', 'Permite visualizar descuentos y promociones vigentes', NOW(), NOW()),
    ('PROMOTIONS_MANAGE', 'Administrar Promociones', 'FINANZAS', 'Permite crear, editar y desactivar promociones comerciales', NOW(), NOW()),

    -- REPORTES
    ('REPORTS_VIEW', 'Visualizar Reportes y Carnet QR', 'REPORTES', 'Permite generar reportes PDF y carnets digitales QR', NOW(), NOW());

-- ============================================================
-- ROLE PERMISSIONS ASSIGNMENTS
-- ============================================================
-- 1. SUPER_ADMIN: Todos los permisos del sistema
INSERT INTO role_permissions (id_role, id_permission, created_at)
SELECT r.id, p.id, NOW()
FROM roles r
CROSS JOIN permissions p
WHERE r.name = 'SUPER_ADMIN';

-- 2. ADMIN: Todos los permisos del negocio excepto ROLES_MANAGE y PERMISSIONS_MANAGE (exclusivos del propietario SUPER_ADMIN)
INSERT INTO role_permissions (id_role, id_permission, created_at)
SELECT r.id, p.id, NOW()
FROM roles r
CROSS JOIN permissions p
WHERE r.name = 'ADMIN'
  AND p.code NOT IN ('ROLES_MANAGE', 'PERMISSIONS_MANAGE');

-- 3. RECEPCIONISTA: Operación de clientes, membresías, clases, asistencias y cobros
INSERT INTO role_permissions (id_role, id_permission, created_at)
SELECT r.id, p.id, NOW()
FROM roles r
CROSS JOIN permissions p
WHERE r.name = 'RECEPCIONISTA'
  AND p.code IN (
    'BRANCHES_READ',
    'MEMBERS_READ', 'MEMBERS_CREATE', 'MEMBERS_UPDATE',
    'MEMBERSHIPS_READ', 'MEMBERSHIPS_MANAGE', 'PLANS_READ', 'BENEFITS_READ',
    'TRAINERS_READ', 'ASSIGNMENTS_READ', 'ASSIGNMENTS_MANAGE',
    'CLASSES_READ', 'CLASS_TYPES_READ', 'ENROLLMENTS_MANAGE',
    'ATTENDANCES_READ', 'ATTENDANCES_MANAGE', 'WAITLISTS_MANAGE', 'RATINGS_READ',
    'INVOICES_READ', 'INVOICES_MANAGE', 'PAYMENTS_READ', 'PAYMENTS_MANAGE',
    'PAYMENT_METHODS_READ', 'PROMOTIONS_READ',
    'REPORTS_VIEW'
  );

-- 4. ENTRENADOR: Lectura de clientes para seguimiento, entrenamiento, rutinas, ejercicios, medidas físicas y consulta de clases (SIN Membresías ni Finanzas)
INSERT INTO role_permissions (id_role, id_permission, created_at)
SELECT r.id, p.id, NOW()
FROM roles r
CROSS JOIN permissions p
WHERE r.name = 'ENTRENADOR'
  AND p.code IN (
    'BRANCHES_READ',
    'MEMBERS_READ',
    'GOALS_READ', 'GOALS_MANAGE', 'MEASUREMENTS_READ', 'MEASUREMENTS_MANAGE',
    'TRAINERS_READ', 'SPECIALIZATIONS_READ', 'SPECIALIZATIONS_MANAGE',
    'ASSIGNMENTS_READ', 'ASSIGNMENTS_MANAGE',
    'EXERCISES_READ', 'EXERCISES_MANAGE',
    'ROUTINES_READ', 'ROUTINES_MANAGE',
    'WORKOUT_LOGS_READ', 'WORKOUT_LOGS_MANAGE',
    'CLASSES_READ', 'CLASS_TYPES_READ', 'WAITLISTS_MANAGE', 'ATTENDANCES_READ', 'RATINGS_READ'
  );

-- 5. CLIENTE: Autogestión de entrenamiento, salud y clases (SIN Administración, SIN Membresías y SIN Finanzas/Reportes)
INSERT INTO role_permissions (id_role, id_permission, created_at)
SELECT r.id, p.id, NOW()
FROM roles r
CROSS JOIN permissions p
WHERE r.name = 'CLIENTE'
  AND p.code IN (
    'GOALS_READ', 'GOALS_MANAGE', 'MEASUREMENTS_READ',
    'ASSIGNMENTS_READ', 'EXERCISES_READ', 'ROUTINES_READ',
    'WORKOUT_LOGS_READ', 'WORKOUT_LOGS_MANAGE',
    'CLASSES_READ', 'ENROLLMENTS_MANAGE',
    'ATTENDANCES_READ', 'WAITLISTS_MANAGE', 'RATINGS_READ', 'RATINGS_CREATE'
  );

-- ============================================================
-- USERS
-- ============================================================
-- Propietario / Superusuario del Sistema (Usuario Dios)
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
        (SELECT id FROM roles WHERE name = 'SUPER_ADMIN'),
        'Super',
        'Propietario',
        'superadmin@nova.com',
        'superadmin',
        '$2a$10$5ObnwRfhZWYW6Y6/v1S92eOwCWPHYgMi1KL4RbUSAiVk6KDfJSD6q',
        TRUE,
        FALSE,
        NOW(),
        NOW());

-- Administrador del Negocio
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

-- Cliente base
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

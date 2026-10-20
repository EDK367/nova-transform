# Nova Transform - Backend API

Sistema integral de gestión de gimnasios y centros de acondicionamiento físico de alto rendimiento, desarrollado con Quarkus y Java 21. Proporciona una arquitectura robusta, escalable y segura para la administración completa de sucursales, personal, socios, membresías, clases, facturación, rutinas de entrenamiento, generación de comprobantes y credenciales en PDF con código QR y notificaciones por correo electrónico mediante Resend.

---

## 🛠️ Stack Tecnológico

- **Lenguaje:** Java 21 (LTS)
- **Framework:** Quarkus 3.39.1
- **Base de Datos:** PostgreSQL 14+ con migraciones gestionadas por Flyway
- **Persistencia:** Hibernate ORM con Panache (Patrón Repository)
- **Seguridad y Autenticación:** SmallRye JWT (RS256 con par de claves pública/privada), Quarkus Security
- **Validaciones:** Hibernate Validator / Jakarta Validation
- **Mapeo de Entidades / DTOs:** MapStruct 1.6.3 + Lombok
- **Documentación de API:** Scalar UI interactivo (`/scalar`, `/docs`) y SmallRye OpenAPI / Swagger UI (`/swagger`)
- **Generación de Reportes PDF:** OpenPDF 2.0.3 (Facturas oficiales, carnet digital con QR, reportes de asistencia)
- **Generación de Códigos QR:** Google ZXing 3.5.3
- **Servicio de Correos:** Resend API (vía HTTP Client asíncrono con plantillas HTML responsivas)

---

## 🏛️ Arquitectura del Proyecto

El proyecto sigue una arquitectura en capas limpia y desacoplada:

```
src/main/java/org/nova/
├── common/             # Estructuras genéricas de respuesta (ApiResponse, PageResponse, ErrorResponse)
├── domain/
│   ├── enums/          # Enumeraciones de negocio (MembershipStatus, InvoiceStatus, TokenType, etc.)
│   └── model/          # Entidades JPA Panache mapeadas contra el esquema relacional
├── exception/          # Manejadores y excepciones globales (NotFoundException, ConflictException, etc.)
├── repository/         # Repositorios PanacheRepositoryBase con consultas optimizadas
├── service/            # Interfaces de servicios de lógica de negocio
│   └── impl/           # Implementaciones con validaciones transaccionales y notificaciones
├── util/               # Utilidades (PasswordGenerator, etc.)
└── web/
    ├── dto/            # Records inmutables de petición y respuesta con validaciones Jakarta
    ├── mapper/         # Interfaces MapStruct para transformación bidireccional DTO-Entidad
    └── resource/       # Controladores REST Jakarta REST (JAX-RS) con seguridad basada en roles
```

---

## 🔐 Roles y Seguridad

El sistema maneja control de acceso basado en roles (RBAC):
- **`ADMIN`**: Acceso total a todos los módulos y operaciones administrativas.
- **`RECEPCIONISTA`**: Gestión de socios, asistencias, cobros, facturación, membresías y clases.
- **`ENTRENADOR`**: Gestión de asignaciones de entrenamiento, ejercicios, rutinas, clases y mediciones físicas.
- **`CLIENTE`**: Acceso a perfil propio, consulta de membresías, historial de pagos, rutinas, descarga de carnet digital y comprobantes.

---

## 📖 Documentación Interactiva de la API

La aplicación cuenta con dos interfaces de documentación interactiva:
- **Scalar UI (Recomendado):** `http://localhost:8080/scalar` o `http://localhost:8080/docs`
- **Swagger UI:** `http://localhost:8080/swagger`
- **Especificación OpenAPI (JSON/YAML):** `http://localhost:8080/openapi`

---

## 📌 Catálogo de Endpoints REST

### 1. Autenticación (`/api/v1/auth`)
| Método | Ruta | Roles | Descripción |
|---|---|---|---|
| `POST` | `/api/v1/auth/login` | PermitAll | Autenticación con credenciales y emisión de token JWT |

### 2. Usuarios y Roles (`/api/v1/users`, `/api/v1/roles`)
| Método | Ruta | Roles | Descripción |
|---|---|---|---|
| `GET` | `/api/v1/users` | ADMIN | Listado paginado de usuarios |
| `GET` | `/api/v1/users/{id}` | ADMIN | Detalle de un usuario |
| `POST` | `/api/v1/users` | ADMIN | Creación de usuario interno con credenciales temporales |
| `PUT` | `/api/v1/users/{id}` | ADMIN | Actualización de datos de usuario |
| `DELETE` | `/api/v1/users/{id}` | ADMIN | Eliminación de usuario |
| `GET` | `/api/v1/roles` | ADMIN | Catálogo de roles del sistema |

### 3. Sucursales (`/api/v1/branches`)
| Método | Ruta | Roles | Descripción |
|---|---|---|---|
| `GET` | `/api/v1/branches` | Todos | Listado de sucursales (filtro por activas) |
| `GET` | `/api/v1/branches/{id}` | Todos | Detalle de una sucursal |
| `POST` | `/api/v1/branches` | ADMIN | Registro de nueva sucursal |
| `PUT` | `/api/v1/branches/{id}` | ADMIN | Actualización de datos de sucursal |
| `DELETE` | `/api/v1/branches/{id}` | ADMIN | Eliminación de sucursal |

### 4. Personal: Recepcionistas y Entrenadores (`/api/v1/receptionists`, `/api/v1/trainers`)
| Método | Ruta | Roles | Descripción |
|---|---|---|---|
| `GET` | `/api/v1/receptionists` | ADMIN | Listado de recepcionistas |
| `GET` | `/api/v1/receptionists/{id}` | ADMIN | Detalle de un recepcionista |
| `POST` | `/api/v1/receptionists` | ADMIN | Alta de recepcionista y envío de credenciales por correo |
| `PUT` | `/api/v1/receptionists/{id}` | ADMIN | Modificación de recepcionista |
| `DELETE` | `/api/v1/receptionists/{id}` | ADMIN | Baja de recepcionista |
| `GET` | `/api/v1/trainers` | Todos | Listado de entrenadores |
| `GET` | `/api/v1/trainers/{id}` | Todos | Detalle de entrenador |
| `POST` | `/api/v1/trainers` | ADMIN | Alta de entrenador con notificación por correo |
| `PUT` | `/api/v1/trainers/{id}` | ADMIN | Modificación de entrenador |
| `DELETE` | `/api/v1/trainers/{id}` | ADMIN | Baja de entrenador |

### 5. Especializaciones de Entrenador (`/api/v1/specializations`, `/api/v1/trainer-specializations`)
| Método | Ruta | Roles | Descripción |
|---|---|---|---|
| `GET` | `/api/v1/specializations` | Todos | Catálogo de especializaciones |
| `POST` | `/api/v1/specializations` | ADMIN | Alta de especialización |
| `PUT` | `/api/v1/specializations/{id}` | ADMIN | Actualización de especialización |
| `DELETE` | `/api/v1/specializations/{id}` | ADMIN | Eliminación de especialización |
| `GET` | `/api/v1/trainer-specializations` | Todos | Especializaciones asignadas a entrenadores |
| `POST` | `/api/v1/trainer-specializations` | ADMIN | Asignar especialización a un entrenador |
| `DELETE` | `/api/v1/trainer-specializations/{id}` | ADMIN | Desvincular especialización |

### 6. Socios (`/api/v1/members`)
| Método | Ruta | Roles | Descripción |
|---|---|---|---|
| `GET` | `/api/v1/members` | ADMIN, RECEPCIONISTA | Listado paginado de socios |
| `GET` | `/api/v1/members/{id}` | ADMIN, RECEPCIONISTA | Detalle del socio |
| `POST` | `/api/v1/members` | ADMIN, RECEPCIONISTA | Registro de socio y envío automático de bienvenida por correo |
| `DELETE` | `/api/v1/members/{id}` | ADMIN | Eliminación de socio |
| `GET` | `/api/v1/members/{id}/carnet-pdf` | Todos | Visualización de carnet digital con código QR |
| `POST` | `/api/v1/members/{id}/solicitar-carnet` | ADMIN, RECEP, CLIENTE | Descarga oficial de la credencial de socio en PDF |

### 7. Membresías y Planes (`/api/v1/memberships`, `/api/v1/membership-plans`)
| Método | Ruta | Roles | Descripción |
|---|---|---|---|
| `GET` | `/api/v1/membership-plans` | Todos | Catálogo de planes y precios |
| `POST` | `/api/v1/membership-plans` | ADMIN | Creación de plan de membresía |
| `PUT` | `/api/v1/membership-plans/{id}` | ADMIN | Modificación de plan de membresía |
| `DELETE` | `/api/v1/membership-plans/{id}` | ADMIN | Eliminación de plan de membresía |
| `GET` | `/api/v1/membership-plans/{id}/benefits` | Todos | Beneficios incluidos en el plan |
| `POST` | `/api/v1/membership-plans/{id}/benefits/{benefitId}` | ADMIN | Asociar beneficio al plan |
| `DELETE` | `/api/v1/membership-plans/{id}/benefits/{benefitId}` | ADMIN | Desasociar beneficio del plan |
| `GET` | `/api/v1/membership-plans/{id}/price-history` | ADMIN, RECEPCIONISTA | Historial de variaciones de precio |
| `GET` | `/api/v1/memberships` | ADMIN, RECEPCIONISTA | Listado y filtros de membresías |
| `GET` | `/api/v1/memberships/my` | CLIENTE | Membresías del socio autenticado |
| `POST` | `/api/v1/memberships` | ADMIN, RECEPCIONISTA | Suscripción de socio a un plan |
| `PUT` | `/api/v1/memberships/{id}/change-plan` | ADMIN, RECEPCIONISTA | Cambio de plan de membresía |
| `POST` | `/api/v1/memberships/{id}/freeze` | ADMIN, RECEPCIONISTA | Congelamiento temporal de membresía |
| `POST` | `/api/v1/memberships/{id}/unfreeze` | ADMIN, RECEPCIONISTA | Descongelamiento y reactivación |
| `POST` | `/api/v1/memberships/{id}/cancel` | ADMIN | Cancelación definitiva de membresía |
| `GET` | `/api/v1/memberships/{id}/history` | ADMIN, RECEP, CLIENTE | Historial de estados de la membresía |
| `GET` | `/api/v1/memberships/{id}/freezes` | ADMIN, RECEP, CLIENTE | Historial de congelamientos |

### 8. Facturación y Pagos (`/api/v1/invoices`, `/api/v1/payment-receipts`, `/api/v1/payment-methods`)
| Método | Ruta | Roles | Descripción |
|---|---|---|---|
| `GET` | `/api/v1/invoices` | ADMIN, RECEPCIONISTA | Listado paginado de facturas con filtros |
| `GET` | `/api/v1/invoices/my` | CLIENTE | Facturas del socio autenticado |
| `GET` | `/api/v1/invoices/{id}` | ADMIN, RECEP, CLIENTE | Detalle de factura |
| `POST` | `/api/v1/invoices` | ADMIN, RECEPCIONISTA | Emisión de nueva factura |
| `POST` | `/api/v1/invoices/{id}/cancel` | ADMIN | Cancelación de factura |
| `GET` | `/api/v1/invoices/{id}/pdf` | ADMIN, RECEP, CLIENTE | Descarga de comprobante oficial en formato PDF |
| `GET` | `/api/v1/invoices/{id}/payments` | ADMIN, RECEP, CLIENTE | Pagos aplicados a la factura |
| `GET` | `/api/v1/payment-receipts` | ADMIN, RECEPCIONISTA | Listado de comprobantes de pago |
| `POST` | `/api/v1/payment-receipts` | ADMIN, RECEPCIONISTA | Registro de pago (envía comprobante al correo) |
| `GET` | `/api/v1/payment-methods` | Todos | Métodos de pago aceptados |

### 9. Clases Grupales y Asistencias (`/api/v1/classes`, `/api/v1/attendances`)
| Método | Ruta | Roles | Descripción |
|---|---|---|---|
| `GET` | `/api/v1/classes` | Todos | Consulta de clases programadas |
| `POST` | `/api/v1/classes` | ADMIN, RECEP, ENTRENADOR | Programación de clase |
| `POST` | `/api/v1/class-enrollments` | ADMIN, RECEP, CLIENTE | Inscripción de socio a clase |
| `POST` | `/api/v1/class-ratings` | CLIENTE | Calificación y comentario sobre la clase |
| `POST` | `/api/v1/waitlists` | ADMIN, RECEP, CLIENTE | Ingreso a lista de espera |
| `GET` | `/api/v1/attendances` | ADMIN, RECEPCIONISTA | Registro general de asistencias al gimnasio |
| `POST` | `/api/v1/attendances/check-in` | ADMIN, RECEPCIONISTA | Registro de entrada del socio |
| `POST` | `/api/v1/attendances/{id}/check-out` | ADMIN, RECEPCIONISTA | Registro de salida del socio |

### 10. Entrenamientos y Rutinas (`/api/v1/trainer-assignments`, `/api/v1/exercises`, `/api/v1/routines`)
| Método | Ruta | Roles | Descripción |
|---|---|---|---|
| `GET` | `/api/v1/trainer-assignments` | Todos | Asignaciones de entrenador personal a socios |
| `POST` | `/api/v1/trainer-assignments` | ADMIN, RECEPCIONISTA | Asignar entrenador a un socio |
| `GET` | `/api/v1/exercises` | Todos | Catálogo de ejercicios clasificados por grupo muscular |
| `POST` | `/api/v1/exercises` | ADMIN, ENTRENADOR | Alta de nuevo ejercicio |
| `GET` | `/api/v1/routines` | Todos | Rutinas de entrenamiento por asignación o socio |
| `POST` | `/api/v1/routines` | ADMIN, ENTRENADOR | Creación de rutina personalizada |
| `GET` | `/api/v1/routine-exercises` | Todos | Ejercicios programados por día en cada rutina |
| `POST` | `/api/v1/routine-exercises` | ADMIN, ENTRENADOR | Agregar ejercicio con series, repeticiones y descansos |
| `GET` | `/api/v1/workout-logs` | Todos | Bitácora de ejecución de ejercicios |
| `POST` | `/api/v1/workout-logs` | ADMIN, ENTRENADOR, CLIENTE | Registro de series, repeticiones y pesos logrados |

### 11. Objetivos y Mediciones Físicas (`/api/v1/goals`, `/api/v1/physical-measurements`)
| Método | Ruta | Roles | Descripción |
|---|---|---|---|
| `GET` | `/api/v1/goals` | Todos | Catálogo de objetivos físicos y deportivos |
| `POST` | `/api/v1/goals` | ADMIN, ENTRENADOR | Creación de nuevo objetivo |
| `GET` | `/api/v1/member-goals` | Todos | Metas y objetivos asignados al socio |
| `POST` | `/api/v1/member-goals` | ADMIN, ENTRENADOR, CLIENTE | Asignar meta deportiva al socio |
| `GET` | `/api/v1/physical-measurements` | Todos | Historial de evolución de medidas antropométricas |
| `POST` | `/api/v1/physical-measurements` | ADMIN, ENTRENADOR | Registro de nueva medición física |

### 12. Centro de Reportes PDF (`/api/v1/reports`)
| Método | Ruta | Roles | Descripción |
|---|---|---|---|
| `GET` | `/api/v1/reports/invoices/{id}/pdf` | ADMIN, RECEP, CLIENTE | Comprobante de pago/factura oficial en PDF |
| `GET` | `/api/v1/reports/members/{id}/carnet-pdf` | Todos | Credencial digital de socio en PDF con código QR |
| `GET` | `/api/v1/reports/attendances/pdf` | ADMIN, RECEP, ENTRENADOR | Reporte consolidado de asistencias en PDF |

---

## ⚙️ Configuración del Entorno

Crear o ajustar el archivo `.env` en la raíz del proyecto:

```env
DB_USER=postgres
DB_PASSWORD=admin
DB_URL=jdbc:postgresql://localhost:5432
DB_NAME=novadb

RESEND_API_KEY=re_ALuNQmia_GVHSuQZ7yR3scJWziMaRLXjT
RESEND_FROM=Nova Transform <soporte@solairy.app>
```

---

## 🚀 Ejecución del Proyecto

### Modo Desarrollo (Hot-Reload)
```bash
./mvnw quarkus:dev
```

### Compilar y Ejecutar Pruebas Automatizadas
```bash
./mvnw clean test -Dquarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/novadb -Dquarkus.datasource.username=postgres -Dquarkus.datasource.password=admin -Dquarkus.datasource.devservices.enabled=false
```

### Empaquetar JAR de Producción
```bash
./mvnw clean package
java -jar target/quarkus-app/quarkus-run.jar
```

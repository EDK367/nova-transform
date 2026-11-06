package org.nova.security;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jboss.logging.Logger;

@ApplicationScoped
@RequiredArgsConstructor
public class RolePermissionSyncBootstrap {

    private static final Logger LOG = Logger.getLogger(RolePermissionSyncBootstrap.class);

    private final EntityManager entityManager;

    @Transactional
    public void onStart(@Observes StartupEvent event) {
        try {
            // Limpiar cualquier permiso de Administración, Membresías, Finanzas, Reportes o Staff en CLIENTE
            int removedFromClient = entityManager.createNativeQuery("""
                    DELETE FROM role_permissions
                    WHERE id_role = (SELECT id FROM roles WHERE name = 'CLIENTE')
                      AND id_permission IN (
                          SELECT id FROM permissions
                          WHERE module IN ('SEGURIDAD', 'USUARIOS', 'SUCURSALES', 'MEMBRESIAS', 'FINANZAS', 'REPORTES')
                             OR code IN ('MEMBERS_READ', 'MEMBERS_CREATE', 'MEMBERS_UPDATE', 'MEMBERS_DELETE',
                                         'TRAINERS_READ', 'TRAINERS_MANAGE', 'SPECIALIZATIONS_READ', 'SPECIALIZATIONS_MANAGE',
                                         'ASSIGNMENTS_MANAGE', 'EXERCISES_MANAGE', 'ROUTINES_MANAGE',
                                         'CLASSES_MANAGE', 'CLASS_TYPES_READ', 'CLASS_TYPES_MANAGE', 'ATTENDANCES_MANAGE',
                                         'MEASUREMENTS_MANAGE')
                      )
                    """).executeUpdate();

            // Limpiar cualquier permiso de Administración, Membresías, Finanzas o Reportes en ENTRENADOR
            int removedFromTrainer = entityManager.createNativeQuery("""
                    DELETE FROM role_permissions
                    WHERE id_role = (SELECT id FROM roles WHERE name = 'ENTRENADOR')
                      AND id_permission IN (
                          SELECT id FROM permissions
                          WHERE module IN ('SEGURIDAD', 'USUARIOS', 'MEMBRESIAS', 'FINANZAS', 'REPORTES')
                             OR code IN ('BRANCHES_MANAGE', 'MEMBERS_CREATE', 'MEMBERS_UPDATE', 'MEMBERS_DELETE',
                                         'TRAINERS_MANAGE', 'CLASSES_MANAGE', 'CLASS_TYPES_MANAGE',
                                         'ATTENDANCES_MANAGE')
                      )
                    """).executeUpdate();

            // Asegurar que ENTRENADOR tenga permiso de lectura de clientes (MEMBERS_READ) y gestión de asignaciones (ASSIGNMENTS_MANAGE) para registrar medidas, metas, rutinas y bitácoras
            int grantedToTrainer = entityManager.createNativeQuery("""
                    INSERT INTO role_permissions (id_role, id_permission, created_at)
                    SELECT r.id, p.id, NOW()
                    FROM roles r
                    JOIN permissions p ON p.code IN ('MEMBERS_READ', 'ASSIGNMENTS_MANAGE')
                    WHERE r.name = 'ENTRENADOR'
                      AND NOT EXISTS (
                          SELECT 1 FROM role_permissions rp
                          WHERE rp.id_role = r.id AND rp.id_permission = p.id
                      )
                    """).executeUpdate();

            // Actualizar terminología de módulo SOCIOS -> CLIENTES en el catálogo de permisos
            entityManager.createNativeQuery("""
                    UPDATE permissions
                    SET module = 'CLIENTES',
                        name = REPLACE(REPLACE(name, 'Socios', 'Clientes'), 'Socio', 'Cliente'),
                        description = REPLACE(REPLACE(description, 'socios', 'clientes'), 'socio', 'cliente'),
                        updated_at = NOW()
                    WHERE module = 'SOCIOS' OR name LIKE '%Socio%' OR description LIKE '%socio%'
                    """).executeUpdate();

            if (removedFromClient > 0 || removedFromTrainer > 0 || grantedToTrainer > 0) {
                LOG.infof("Sincronización de seguridad completada: %d permisos removidos de CLIENTE, %d removidos de ENTRENADOR, %d asignados a ENTRENADOR.",
                        removedFromClient, removedFromTrainer, grantedToTrainer);
            }
        } catch (Exception e) {
            LOG.debug("Omitiendo sincronización inicial de permisos: " + e.getMessage());
        }
    }
}

package org.nova.service.impl.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.user.Permission;
import org.nova.domain.model.user.Role;
import org.nova.exception.BadRequestException;
import org.nova.exception.ConflictException;
import org.nova.exception.NotFoundException;
import org.nova.repository.PermissionRepository;
import org.nova.repository.RoleRepository;
import org.nova.service.user.PermissionService;
import org.nova.web.dto.request.permission.CreatePermissionRequest;
import org.nova.web.dto.request.permission.UpdatePermissionRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private static final Set<String> PROTECTED_CODES = Set.of("ROLES_MANAGE", "PERMISSIONS_MANAGE", "ROLES_READ", "PERMISSIONS_READ");

    private final PermissionRepository repository;
    private final RoleRepository roleRepository;

    @Override
    public List<Permission> findAll(String module) {
        if (module != null && !module.isBlank()) {
            return repository.findByModule(module);
        }
        return repository.listAllOrdered();
    }

    @Override
    public Optional<Permission> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public Permission create(CreatePermissionRequest request) {
        String normalizedCode = normalizeCode(request.code());
        String normalizedModule = request.module().trim().toUpperCase();

        if (repository.existsByCode(normalizedCode)) {
            throw new ConflictException("Ya existe un permiso registrado con el código: " + normalizedCode);
        }

        Permission permission = new Permission();
        permission.setCode(normalizedCode);
        permission.setName(request.name().trim());
        permission.setModule(normalizedModule);
        permission.setDescription(request.description() != null ? request.description().trim() : null);

        repository.persist(permission);

        // Garantizar que el propietario SUPER_ADMIN tenga siempre todos los permisos
        roleRepository.findByName("SUPER_ADMIN").ifPresent(superAdmin -> {
            superAdmin.getPermissions().add(permission);
        });

        return permission;
    }

    @Override
    @Transactional
    public Permission update(Long id, UpdatePermissionRequest request) {
        Permission existing = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Permiso no encontrado con ID: " + id));

        String normalizedCode = normalizeCode(request.code());
        String normalizedModule = request.module().trim().toUpperCase();

        if (PROTECTED_CODES.contains(existing.getCode()) && !existing.getCode().equalsIgnoreCase(normalizedCode)) {
            throw new BadRequestException("No se puede modificar el código de un permiso crítico de seguridad: " + existing.getCode());
        }

        if (!existing.getCode().equalsIgnoreCase(normalizedCode) && repository.existsByCode(normalizedCode)) {
            throw new ConflictException("Ya existe otro permiso con el código: " + normalizedCode);
        }

        existing.setCode(normalizedCode);
        existing.setName(request.name().trim());
        existing.setModule(normalizedModule);
        existing.setDescription(request.description() != null ? request.description().trim() : null);

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Permission existing = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Permiso no encontrado con ID: " + id));

        if (PROTECTED_CODES.contains(existing.getCode())) {
            throw new BadRequestException("No está permitido eliminar un permiso crítico de seguridad del sistema: " + existing.getCode());
        }

        List<Role> allRoles = roleRepository.listAll();
        for (Role role : allRoles) {
            if (role.getPermissions() != null) {
                role.getPermissions().removeIf(p -> p.getId().equals(id));
            }
        }

        repository.delete(existing);
    }

    private String normalizeCode(String code) {
        if (code == null || code.isBlank()) {
            throw new BadRequestException("El código del permiso es obligatorio");
        }
        return code.trim().toUpperCase().replaceAll("[^A-Z0-9_]", "_");
    }
}

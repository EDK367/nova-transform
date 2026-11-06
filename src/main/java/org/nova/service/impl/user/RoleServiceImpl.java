package org.nova.service.impl.user;

import io.quarkus.panache.common.Sort;
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
import org.nova.repository.UserRepository;
import org.nova.service.user.RoleService;
import org.nova.web.dto.request.role.CreateRoleRequest;
import org.nova.web.dto.request.role.UpdateRoleRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;

    @Override
    public List<Role> findAll() {
        return repository.listAll(Sort.by("id"));
    }

    @Override
    public Optional<Role> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public Role create(Role role) {
        repository.persist(role);
        return role;
    }

    @Override
    @Transactional
    public Role createRole(CreateRoleRequest request) {
        String normalizedName = normalizeRoleName(request.name());

        if ("SUPER_ADMIN".equalsIgnoreCase(normalizedName)) {
            throw new BadRequestException("El rol SUPER_ADMIN es exclusivo del propietario del sistema");
        }

        if (repository.existsByName(normalizedName)) {
            throw new ConflictException("Ya existe un rol registrado con el nombre: " + normalizedName);
        }

        Role role = new Role();
        role.setName(normalizedName);
        role.setDescription(request.description() != null ? request.description().trim() : null);
        role.setSystemRole(false);
        role.setPermissions(resolvePermissions(request.permissionIds()));

        repository.persist(role);
        return role;
    }

    @Override
    @Transactional
    public Role updateRole(Long id, UpdateRoleRequest request) {
        Role existing = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Rol no encontrado con ID: " + id));

        if ("SUPER_ADMIN".equalsIgnoreCase(existing.getName())) {
            throw new BadRequestException("El rol SUPER_ADMIN del propietario del sistema es inmutable y posee acceso total");
        }

        String normalizedName = normalizeRoleName(request.name());

        if (existing.isSystemRole() && !existing.getName().equalsIgnoreCase(normalizedName)) {
            throw new BadRequestException("No se puede cambiar el código identificador de un rol nativo del sistema (" + existing.getName() + ")");
        }

        if (!existing.getName().equalsIgnoreCase(normalizedName) && repository.existsByName(normalizedName)) {
            throw new ConflictException("Ya existe otro rol con el nombre: " + normalizedName);
        }

        if (!existing.isSystemRole()) {
            existing.setName(normalizedName);
        }
        existing.setDescription(request.description() != null ? request.description().trim() : null);

        if (request.permissionIds() != null) {
            existing.setPermissions(resolvePermissions(request.permissionIds()));
        }

        return existing;
    }

    @Override
    @Transactional
    public Role updateRolePermissions(Long id, List<Long> permissionIds) {
        Role existing = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Rol no encontrado con ID: " + id));

        if ("SUPER_ADMIN".equalsIgnoreCase(existing.getName())) {
            throw new BadRequestException("El rol SUPER_ADMIN del propietario del sistema no admite revocación ni modificación de permisos");
        }

        existing.setPermissions(resolvePermissions(permissionIds));
        return existing;
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        Role existing = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Rol no encontrado con ID: " + id));

        if (existing.isSystemRole() || "SUPER_ADMIN".equalsIgnoreCase(existing.getName())) {
            throw new BadRequestException("No está permitido eliminar un rol nativo del sistema: " + existing.getName());
        }

        long assignedUsers = userRepository.countByRoleId(id);
        if (assignedUsers > 0) {
            throw new ConflictException("No se puede eliminar el rol '" + existing.getName() + "' porque tiene " + assignedUsers + " usuario(s) asignado(s)");
        }

        existing.getPermissions().clear();
        repository.delete(existing);
    }

    private Set<Permission> resolvePermissions(List<Long> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            return new HashSet<>();
        }
        List<Permission> found = permissionRepository.findAllByIds(permissionIds);
        return new HashSet<>(found);
    }

    private String normalizeRoleName(String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("El nombre del rol es obligatorio");
        }
        return name.trim().toUpperCase().replaceAll("[^A-Z0-9_]", "_");
    }
}

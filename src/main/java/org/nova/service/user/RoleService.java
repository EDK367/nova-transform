package org.nova.service.user;

import org.nova.domain.model.user.Role;
import org.nova.web.dto.request.role.CreateRoleRequest;
import org.nova.web.dto.request.role.UpdateRoleRequest;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    List<Role> findAll();

    Optional<Role> findById(Long id);

    Role create(Role role);

    Role createRole(CreateRoleRequest request);

    Role updateRole(Long id, UpdateRoleRequest request);

    Role updateRolePermissions(Long id, List<Long> permissionIds);

    void deleteRole(Long id);
}

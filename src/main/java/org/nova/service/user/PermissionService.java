package org.nova.service.user;

import org.nova.domain.model.user.Permission;
import org.nova.web.dto.request.permission.CreatePermissionRequest;
import org.nova.web.dto.request.permission.UpdatePermissionRequest;

import java.util.List;
import java.util.Optional;

public interface PermissionService {

    List<Permission> findAll(String module);

    Optional<Permission> findById(Long id);

    Permission create(CreatePermissionRequest request);

    Permission update(Long id, UpdatePermissionRequest request);

    void delete(Long id);
}

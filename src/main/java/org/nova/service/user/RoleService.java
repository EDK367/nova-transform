package org.nova.service.user;

import org.nova.domain.model.user.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    List<Role> findAll();

    Optional<Role> findById(Long id);

    Role create(Role role);

    Role update(Long id, Role role);

    void delete(Long id);
}
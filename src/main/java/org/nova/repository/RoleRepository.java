package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.user.Role;

import java.util.Optional;

@ApplicationScoped
public class RoleRepository implements PanacheRepositoryBase<Role, Long> {

    public Optional<Role> findByName(String name) {
        return find("UPPER(name) = ?1", name != null ? name.trim().toUpperCase() : "").firstResultOptional();
    }

    public boolean existsByName(String name) {
        return count("UPPER(name) = ?1", name != null ? name.trim().toUpperCase() : "") > 0;
    }
}

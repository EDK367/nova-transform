package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.user.Permission;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PermissionRepository implements PanacheRepositoryBase<Permission, Long> {

    public Optional<Permission> findByCode(String code) {
        return find("UPPER(code) = ?1", code != null ? code.trim().toUpperCase() : "").firstResultOptional();
    }

    public boolean existsByCode(String code) {
        return count("UPPER(code) = ?1", code != null ? code.trim().toUpperCase() : "") > 0;
    }

    public List<Permission> findByModule(String module) {
        return list("UPPER(module) = ?1", Sort.by("code"), module != null ? module.trim().toUpperCase() : "");
    }

    public List<Permission> listAllOrdered() {
        return listAll(Sort.by("module").and("code"));
    }

    public List<Permission> findAllByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return list("id in ?1", ids);
    }
}

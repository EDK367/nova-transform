package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.member.Goal;

@ApplicationScoped
public class GoalRepository implements PanacheRepositoryBase<Goal, Long> {

    public boolean existsByName(String name) {
        return count("LOWER(name) = ?1", name.toLowerCase()) > 0;
    }

    public boolean existsByNameAndIdNot(String name, Long id) {
        return count("LOWER(name) = ?1 and id != ?2", name.toLowerCase(), id) > 0;
    }
}

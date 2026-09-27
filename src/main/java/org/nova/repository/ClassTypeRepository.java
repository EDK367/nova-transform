package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.gymclass.ClassType;

import java.util.Optional;

@ApplicationScoped
public class ClassTypeRepository implements PanacheRepositoryBase<ClassType, Long> {

    public Optional<ClassType> findByName(String name) {
        return find("name", name).firstResultOptional();
    }

    public boolean existsByName(String name) {
        return count("name", name) > 0;
    }

    public boolean existsByNameAndIdNot(String name, Long id) {
        return count("name = ?1 and id != ?2", name, id) > 0;
    }
}

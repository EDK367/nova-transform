package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.membership.Promotion;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PromotionRepository implements PanacheRepositoryBase<Promotion, Long> {

    public Optional<Promotion> findByName(String name) {
        return find("name", name).firstResultOptional();
    }

    public boolean existsByName(String name) {
        return count("name", name) > 0;
    }

    public boolean existsByNameAndIdNot(String name, Long id) {
        return count("name = ?1 and id != ?2", name, id) > 0;
    }

    public List<Promotion> listActive(LocalDate date) {
        return find("active = true and startDate <= ?1 and endDate >= ?1 order by startDate desc", date).list();
    }
}
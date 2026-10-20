package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.trainer.Trainer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class TrainerRepository implements PanacheRepositoryBase<Trainer, UUID> {

    public List<Trainer> listAllWithUser() {
        return find("select t from Trainer t left join fetch t.users u left join fetch u.role left join fetch u.branch").list();
    }

    public Optional<Trainer> findByIdWithUser(UUID id) {
        return find("select t from Trainer t left join fetch t.users u left join fetch u.role left join fetch u.branch where t.id = ?1", id).firstResultOptional();
    }
}

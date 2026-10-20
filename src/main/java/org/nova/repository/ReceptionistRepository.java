package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.staff.Receptionist;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ReceptionistRepository implements PanacheRepositoryBase<Receptionist, UUID> {

    public List<Receptionist> listAllWithUser() {
        return find("select r from Receptionist r left join fetch r.users u left join fetch u.role left join fetch u.branch").list();
    }

    public Optional<Receptionist> findByIdWithUser(UUID id) {
        return find("select r from Receptionist r left join fetch r.users u left join fetch u.role left join fetch u.branch where r.id = ?1", id).firstResultOptional();
    }
}

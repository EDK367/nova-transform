package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.member.PhysicalMeasurement;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PhysicalMeasurementRepository implements PanacheRepositoryBase<PhysicalMeasurement, Long> {

    public List<PhysicalMeasurement> listAllWithDetails() {
        return find("select pm from PhysicalMeasurement pm left join fetch pm.idMember m left join fetch m.users order by pm.date desc").list();
    }

    public Optional<PhysicalMeasurement> findByIdWithDetails(Long id) {
        return find("select pm from PhysicalMeasurement pm left join fetch pm.idMember m left join fetch m.users where pm.id = ?1", id).firstResultOptional();
    }

    public List<PhysicalMeasurement> findByMemberId(UUID memberId) {
        return find("select pm from PhysicalMeasurement pm left join fetch pm.idMember m left join fetch m.users where pm.idMember.id = ?1 order by pm.date desc", memberId).list();
    }
}

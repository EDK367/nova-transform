package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.enums.ClassStatus;
import org.nova.domain.model.gymclass.Class;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ClassRepository implements PanacheRepositoryBase<Class, Long> {

    public Optional<Class> findByIdWithDetails(Long id) {
        return find("select c from Class c " +
                "join fetch c.idBranch b " +
                "join fetch c.idClassType ct " +
                "join fetch c.idTrainer t " +
                "join fetch t.users u " +
                "where c.id = ?1", id).firstResultOptional();
    }

    public PanacheQuery<Class> findFiltered(
            Long branchId,
            Long classTypeId,
            UUID trainerId,
            ClassStatus status,
            Instant startDate,
            Instant endDate,
            Sort sort
    ) {
        StringBuilder query = new StringBuilder("select c from Class c " +
                "join fetch c.idBranch b " +
                "join fetch c.idClassType ct " +
                "join fetch c.idTrainer t " +
                "join fetch t.users u where 1=1");
        Parameters params = new Parameters();

        if (branchId != null) {
            query.append(" and b.id = :branchId");
            params.and("branchId", branchId);
        }
        if (classTypeId != null) {
            query.append(" and ct.id = :classTypeId");
            params.and("classTypeId", classTypeId);
        }
        if (trainerId != null) {
            query.append(" and t.id = :trainerId");
            params.and("trainerId", trainerId);
        }
        if (status != null) {
            query.append(" and c.status = :status");
            params.and("status", status);
        }
        if (startDate != null) {
            query.append(" and c.schedule >= :startDate");
            params.and("startDate", startDate);
        }
        if (endDate != null) {
            query.append(" and c.schedule <= :endDate");
            params.and("endDate", endDate);
        }

        return find(query.toString(), sort, params);
    }

    public long countByClassTypeId(Long classTypeId) {
        return count("idClassType.id", classTypeId);
    }
}

package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.gymclass.Attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class AttendanceRepository implements PanacheRepositoryBase<Attendance, Long> {

    public Optional<Attendance> findByIdWithDetails(Long id) {
        return find("select a from Attendance a " +
                "join fetch a.idBranch b " +
                "join fetch a.idReceptionist r " +
                "join fetch r.users ru " +
                "join fetch a.idMember m " +
                "join fetch m.users mu " +
                "where a.id = ?1", id).firstResultOptional();
    }

    public List<Attendance> findByMemberId(UUID memberId) {
        return find("select a from Attendance a " +
                "join fetch a.idBranch b " +
                "join fetch a.idReceptionist r " +
                "join fetch r.users ru " +
                "where a.idMember.id = ?1 order by a.date desc, a.entryTime desc", memberId).list();
    }

    public PanacheQuery<Attendance> findFiltered(Long branchId, UUID memberId, LocalDate date, Sort sort) {
        StringBuilder query = new StringBuilder("select a from Attendance a " +
                "join fetch a.idBranch b " +
                "join fetch a.idReceptionist r " +
                "join fetch r.users ru " +
                "join fetch a.idMember m " +
                "join fetch m.users mu where 1=1");
        Parameters params = new Parameters();

        if (branchId != null) {
            query.append(" and b.id = :branchId");
            params.and("branchId", branchId);
        }
        if (memberId != null) {
            query.append(" and m.id = :memberId");
            params.and("memberId", memberId);
        }
        if (date != null) {
            query.append(" and a.date = :date");
            params.and("date", date);
        }

        return find(query.toString(), sort, params);
    }
}

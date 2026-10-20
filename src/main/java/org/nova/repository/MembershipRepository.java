
package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.enums.MembershipStatus;
import org.nova.domain.model.membership.Membership;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class MembershipRepository implements PanacheRepositoryBase<Membership, Long> {

    public List<Membership> findByMemberId(UUID memberId) {
        return find("select m from Membership m join fetch m.idMember mem join fetch mem.users u join fetch m.idPlan p where mem.id = ?1 order by m.createdAt desc", memberId).list();
    }

    public Optional<Membership> findActiveByMemberId(UUID memberId) {
        return find("select m from Membership m join fetch m.idMember mem join fetch mem.users u join fetch m.idPlan p where mem.id = ?1 and m.status = ?2 order by m.createdAt desc", memberId, MembershipStatus.ACTIVE).firstResultOptional();
    }

    public Optional<Membership> findByIdWithDetails(Long id) {
        return find("select m from Membership m join fetch m.idMember mem join fetch mem.users u join fetch m.idPlan p where m.id = ?1", id).firstResultOptional();
    }

    public PanacheQuery<Membership> findFiltered(MembershipStatus status, UUID memberId, Sort sort) {
        StringBuilder query = new StringBuilder("select m from Membership m join fetch m.idMember mem join fetch mem.users u join fetch m.idPlan p where 1=1");
        Parameters params = new Parameters();

        if (status != null) {
            query.append(" and m.status = :status");
            params.and("status", status);
        }

        if (memberId != null) {
            query.append(" and mem.id = :memberId");
            params.and("memberId", memberId);
        }

        Sort qualifiedSort = Sort.by("m.createdAt", Sort.Direction.Descending);

        if (sort != null && !sort.getColumns().isEmpty()) {
            qualifiedSort = Sort.empty();

            for (Sort.Column column : sort.getColumns()) {
                String name = column.getName();

                if (!name.startsWith("m.")) {
                    name = "m." + name;
                }

                qualifiedSort.and(name, column.getDirection());
            }
        }

        return find(query.toString(), qualifiedSort, params);
    }
}

package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.membership.MembershipHistory;

import java.util.List;

@ApplicationScoped
public class MembershipHistoryRepository implements PanacheRepositoryBase<MembershipHistory, Long> {

    public List<MembershipHistory> findByMembershipId(Long membershipId) {
        return find("idMembership.id = ?1 order by changeDate desc", membershipId).list();
    }
}
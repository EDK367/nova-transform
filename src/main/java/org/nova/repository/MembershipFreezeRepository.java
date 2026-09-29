package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.membership.MembershipFreeze;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MembershipFreezeRepository implements PanacheRepositoryBase<MembershipFreeze, Long> {

    public List<MembershipFreeze> findByMembershipId(Long membershipId) {
        return find("idMembership.id = ?1 order by startDate desc", membershipId).list();
    }

    public Optional<MembershipFreeze> findLatestFreeze(Long membershipId) {
        return find("idMembership.id = ?1 order by createdAt desc", membershipId).firstResultOptional();
    }
}
package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.membership.Membership;

@ApplicationScoped
public class MembershipRepository implements PanacheRepositoryBase<Membership, Long> {
}
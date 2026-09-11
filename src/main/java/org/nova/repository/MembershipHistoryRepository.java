package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.membership.MembershipHistory;

@ApplicationScoped
public class MembershipHistoryRepository implements PanacheRepositoryBase<MembershipHistory, Long> {
}
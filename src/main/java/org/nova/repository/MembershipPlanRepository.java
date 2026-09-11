package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.membership.MembershipPlan;

@ApplicationScoped
public class MembershipPlanRepository implements PanacheRepositoryBase<MembershipPlan, Long> {
}
package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.membership.PlanPriceHistory;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PlanPriceHistoryRepository implements PanacheRepositoryBase<PlanPriceHistory, Long> {

    public List<PlanPriceHistory> findByPlanId(Long planId) {
        return find("idPlan.id = ?1 order by startDate desc", planId).list();
    }

    public Optional<PlanPriceHistory> findLatestByPlanId(Long planId) {
        return find("idPlan.id = ?1 order by startDate desc", planId).firstResultOptional();
    }
}
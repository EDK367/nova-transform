package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.membership.PlanPriceHistory;

@ApplicationScoped
public class PlanPriceHistoryRepository implements PanacheRepositoryBase<PlanPriceHistory, Long> {
}
package org.nova.service.membership;

import org.nova.domain.model.membership.PlanPriceHistory;

import java.util.List;
import java.util.Optional;

public interface PlanPriceHistoryService {

    List<PlanPriceHistory> findAll();

    Optional<PlanPriceHistory> findById(Long id);

    PlanPriceHistory create(PlanPriceHistory planPriceHistory);

    PlanPriceHistory update(Long id, PlanPriceHistory planPriceHistory);

    void delete(Long id);
}
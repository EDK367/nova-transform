package org.nova.service.membership;

import org.nova.domain.model.membership.PlanPriceHistory;
import org.nova.web.dto.response.membership.PlanPriceHistoryResponse;

import java.util.List;
import java.util.Optional;

public interface PlanPriceHistoryService {

    List<PlanPriceHistory> findAll();

    List<PlanPriceHistoryResponse> findAllResponses(Long planId);

    Optional<PlanPriceHistory> findById(Long id);

    PlanPriceHistoryResponse findResponseById(Long id);

    PlanPriceHistory create(PlanPriceHistory planPriceHistory);

    PlanPriceHistory update(Long id, PlanPriceHistory planPriceHistory);

    void delete(Long id);
}

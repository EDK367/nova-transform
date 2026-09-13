package org.nova.service.impl.membership;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.membership.PlanPriceHistory;
import org.nova.repository.PlanPriceHistoryRepository;
import org.nova.service.membership.PlanPriceHistoryService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class PlanPriceHistoryServiceImpl implements PlanPriceHistoryService {

    private final PlanPriceHistoryRepository repository;

    @Override
    public List<PlanPriceHistory> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<PlanPriceHistory> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public PlanPriceHistory create(PlanPriceHistory planPriceHistory) {
        repository.persist(planPriceHistory);
        return planPriceHistory;
    }

    @Override
    @Transactional
    public PlanPriceHistory update(Long id, PlanPriceHistory planPriceHistory) {
        PlanPriceHistory existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setIdPlan(planPriceHistory.getIdPlan());
        existing.setPrice(planPriceHistory.getPrice());
        existing.setStartDate(planPriceHistory.getStartDate());
        existing.setEndDate(planPriceHistory.getEndDate());
        existing.setObservation(planPriceHistory.getObservation());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
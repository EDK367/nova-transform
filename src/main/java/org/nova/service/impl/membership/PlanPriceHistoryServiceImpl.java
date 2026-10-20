package org.nova.service.impl.membership;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.membership.PlanPriceHistory;
import org.nova.exception.NotFoundException;
import org.nova.repository.PlanPriceHistoryRepository;
import org.nova.service.membership.PlanPriceHistoryService;
import org.nova.web.dto.response.membership.PlanPriceHistoryResponse;
import org.nova.web.mapper.PlanPriceHistoryMapper;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class PlanPriceHistoryServiceImpl implements PlanPriceHistoryService {

    private final PlanPriceHistoryRepository repository;
    private final PlanPriceHistoryMapper mapper;

    @Override
    public List<PlanPriceHistory> findAll() {
        return repository.listAll();
    }

    @Override
    public List<PlanPriceHistoryResponse> findAllResponses(Long planId) {
        List<PlanPriceHistory> list = planId != null
                ? repository.findByPlanId(planId)
                : repository.listAll();
        return mapper.toResponseList(list);
    }

    @Override
    public Optional<PlanPriceHistory> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    public PlanPriceHistoryResponse findResponseById(Long id) {
        PlanPriceHistory history = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Historial de precio no encontrado con ID: " + id));
        return mapper.toResponse(history);
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

package org.nova.service.impl.membership;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.membership.BenefitPlan;
import org.nova.repository.BenefitPlanRepository;
import org.nova.service.membership.BenefitPlanService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class BenefitPlanServiceImpl implements BenefitPlanService {

    private final BenefitPlanRepository repository;

    @Override
    public List<BenefitPlan> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<BenefitPlan> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public BenefitPlan create(BenefitPlan benefitPlan) {
        repository.persist(benefitPlan);
        return benefitPlan;
    }

    @Override
    @Transactional
    public BenefitPlan update(Long id, BenefitPlan benefitPlan) {
        BenefitPlan existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setIdPlan(benefitPlan.getIdPlan());
        existing.setIdBenefit(benefitPlan.getIdBenefit());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
package org.nova.service.impl.membership;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.membership.Benefit;
import org.nova.domain.model.membership.BenefitPlan;
import org.nova.domain.model.membership.MembershipPlan;
import org.nova.exception.ConflictException;
import org.nova.exception.NotFoundException;
import org.nova.repository.BenefitPlanRepository;
import org.nova.repository.BenefitRepository;
import org.nova.repository.MembershipPlanRepository;
import org.nova.service.membership.BenefitPlanService;
import org.nova.web.dto.request.membership.AssignBenefitPlanRequest;
import org.nova.web.dto.response.membership.BenefitPlanResponse;
import org.nova.web.mapper.BenefitPlanMapper;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class BenefitPlanServiceImpl implements BenefitPlanService {

    private final BenefitPlanRepository repository;
    private final MembershipPlanRepository planRepository;
    private final BenefitRepository benefitRepository;
    private final BenefitPlanMapper mapper;

    @Override
    public List<BenefitPlan> findAll() {
        return repository.listAllWithDetails();
    }

    @Override
    public List<BenefitPlanResponse> findAllResponses(Long planId) {
        List<BenefitPlan> list = planId != null
                ? repository.findByPlanId(planId)
                : repository.listAllWithDetails();
        return mapper.toResponseList(list);
    }

    @Override
    public Optional<BenefitPlan> findById(Long id) {
        return repository.findByIdWithDetails(id);
    }

    @Override
    public BenefitPlanResponse findResponseById(Long id) {
        BenefitPlan bp = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Beneficio de plan no encontrado con ID: " + id));
        return mapper.toResponse(bp);
    }

    @Override
    @Transactional
    public BenefitPlan create(BenefitPlan benefitPlan) {
        repository.persist(benefitPlan);
        return benefitPlan;
    }

    @Override
    @Transactional
    public BenefitPlanResponse assignBenefitToPlan(AssignBenefitPlanRequest request) {
        MembershipPlan plan = planRepository.findByIdOptional(request.planId())
                .orElseThrow(() -> new NotFoundException("Plan no encontrado con ID: " + request.planId()));

        Benefit benefit = benefitRepository.findByIdOptional(request.benefitId())
                .orElseThrow(() -> new NotFoundException("Beneficio no encontrado con ID: " + request.benefitId()));

        if (repository.existsByPlanAndBenefit(request.planId(), request.benefitId())) {
            throw new ConflictException("El beneficio ya esta asignado al plan");
        }

        BenefitPlan bp = new BenefitPlan();
        bp.setIdPlan(plan);
        bp.setIdBenefit(benefit);

        repository.persist(bp);
        return mapper.toResponse(bp);
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

    @Override
    @Transactional
    public void deleteBenefitPlan(Long id) {
        BenefitPlan bp = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Beneficio de plan no encontrado con ID: " + id));
        repository.delete(bp);
    }
}

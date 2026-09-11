package org.nova.service.membership;

import org.nova.domain.model.membership.BenefitPlan;

import java.util.List;
import java.util.Optional;

public interface BenefitPlanService {

    List<BenefitPlan> findAll();

    Optional<BenefitPlan> findById(Long id);

    BenefitPlan create(BenefitPlan benefitPlan);

    BenefitPlan update(Long id, BenefitPlan benefitPlan);

    void delete(Long id);
}
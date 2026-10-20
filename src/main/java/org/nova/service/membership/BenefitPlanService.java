package org.nova.service.membership;

import org.nova.domain.model.membership.BenefitPlan;
import org.nova.web.dto.request.membership.AssignBenefitPlanRequest;
import org.nova.web.dto.response.membership.BenefitPlanResponse;

import java.util.List;
import java.util.Optional;

public interface BenefitPlanService {

    List<BenefitPlan> findAll();

    List<BenefitPlanResponse> findAllResponses(Long planId);

    Optional<BenefitPlan> findById(Long id);

    BenefitPlanResponse findResponseById(Long id);

    BenefitPlan create(BenefitPlan benefitPlan);

    BenefitPlanResponse assignBenefitToPlan(AssignBenefitPlanRequest request);

    BenefitPlan update(Long id, BenefitPlan benefitPlan);

    void delete(Long id);

    void deleteBenefitPlan(Long id);
}

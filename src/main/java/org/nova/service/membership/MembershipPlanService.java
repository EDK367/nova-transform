package org.nova.service.membership;

import org.nova.common.PageResponse;
import org.nova.domain.model.membership.MembershipPlan;
import org.nova.web.dto.request.membership.CreateMembershipPlanRequest;
import org.nova.web.dto.request.membership.UpdateMembershipPlanRequest;
import org.nova.web.dto.response.membership.BenefitResponse;
import org.nova.web.dto.response.membership.MembershipPlanResponse;
import org.nova.web.dto.response.membership.PlanPriceHistoryResponse;

import java.util.List;
import java.util.Optional;

public interface MembershipPlanService {

    List<MembershipPlan> findAll();

    Optional<MembershipPlan> findById(Long id);

    MembershipPlan create(MembershipPlan membershipPlan);

    MembershipPlan update(Long id, MembershipPlan membershipPlan);

    void delete(Long id);

    List<MembershipPlanResponse> findAllResponses();

    PageResponse<MembershipPlanResponse> findPaged(int page, int size, String sortBy, String sortDirection);

    MembershipPlanResponse findResponseById(Long id);

    MembershipPlanResponse createPlan(CreateMembershipPlanRequest request);

    MembershipPlanResponse updatePlan(Long id, UpdateMembershipPlanRequest request);

    void deletePlan(Long id);

    void addBenefitToPlan(Long planId, Long benefitId);

    void removeBenefitFromPlan(Long planId, Long benefitId);

    List<BenefitResponse> getPlanBenefits(Long planId);

    List<PlanPriceHistoryResponse> getPriceHistories(Long planId);
}
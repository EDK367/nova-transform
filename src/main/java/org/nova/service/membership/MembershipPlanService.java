package org.nova.service.membership;

import org.nova.domain.model.membership.MembershipPlan;

import java.util.List;
import java.util.Optional;

public interface MembershipPlanService {

    List<MembershipPlan> findAll();

    Optional<MembershipPlan> findById(Long id);

    MembershipPlan create(MembershipPlan membershipPlan);

    MembershipPlan update(Long id, MembershipPlan membershipPlan);

    void delete(Long id);
}
package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.membership.MembershipPlan;
import org.nova.web.dto.response.membership.BenefitResponse;
import org.nova.web.dto.response.membership.MembershipPlanResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {BenefitMapper.class})
public interface MembershipPlanMapper {

    @Mapping(target = "benefits", ignore = true)
    MembershipPlanResponse toResponse(MembershipPlan plan);

    default MembershipPlanResponse toResponseWithBenefits(MembershipPlan plan, List<BenefitResponse> benefits) {
        if (plan == null) return null;
        return new MembershipPlanResponse(
                plan.getId(),
                plan.getName(),
                plan.getDuration(),
                plan.getPrice(),
                plan.getDescription(),
                benefits,
                plan.getCreatedAt()
        );
    }

    List<MembershipPlanResponse> toResponseList(List<MembershipPlan> plans);
}

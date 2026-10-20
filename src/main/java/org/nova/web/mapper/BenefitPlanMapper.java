package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.membership.BenefitPlan;
import org.nova.web.dto.response.membership.BenefitPlanResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BenefitPlanMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "planId", source = "idPlan.id")
    @Mapping(target = "planName", source = "idPlan.name")
    @Mapping(target = "benefitId", source = "idBenefit.id")
    @Mapping(target = "benefitName", source = "idBenefit.name")
    @Mapping(target = "benefitDescription", source = "idBenefit.description")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    BenefitPlanResponse toResponse(BenefitPlan bp);

    List<BenefitPlanResponse> toResponseList(List<BenefitPlan> list);
}

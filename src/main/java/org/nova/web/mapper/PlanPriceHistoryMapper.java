package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.membership.PlanPriceHistory;
import org.nova.web.dto.response.membership.PlanPriceHistoryResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlanPriceHistoryMapper {

    @Mapping(target = "planId", source = "idPlan.id")
    PlanPriceHistoryResponse toResponse(PlanPriceHistory history);

    List<PlanPriceHistoryResponse> toResponseList(List<PlanPriceHistory> histories);
}

package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.membership.Benefit;
import org.nova.web.dto.response.membership.BenefitResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BenefitMapper {

    BenefitResponse toResponse(Benefit benefit);

    List<BenefitResponse> toResponseList(List<Benefit> benefits);
}

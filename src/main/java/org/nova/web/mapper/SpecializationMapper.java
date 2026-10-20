package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.trainer.Specialization;
import org.nova.web.dto.response.trainer.SpecializationResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SpecializationMapper {

    SpecializationResponse toResponse(Specialization specialization);

    List<SpecializationResponse> toResponseList(List<Specialization> specializations);
}

package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.member.PhysicalMeasurement;
import org.nova.web.dto.response.member.PhysicalMeasurementResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhysicalMeasurementMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "memberId", source = "idMember.id")
    @Mapping(target = "memberName", expression = "java(pm.getIdMember() != null && pm.getIdMember().getUsers() != null ? pm.getIdMember().getUsers().getFirstName() + \" \" + pm.getIdMember().getUsers().getLastName() : null)")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "weight", source = "weight")
    @Mapping(target = "height", source = "height")
    @Mapping(target = "bodyFatPercentage", source = "bodyFatPercentage")
    @Mapping(target = "muscleMass", source = "muscleMass")
    @Mapping(target = "waist", source = "waist")
    @Mapping(target = "arm", source = "arm")
    @Mapping(target = "leg", source = "leg")
    @Mapping(target = "observations", source = "observations")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    PhysicalMeasurementResponse toResponse(PhysicalMeasurement pm);

    List<PhysicalMeasurementResponse> toResponseList(List<PhysicalMeasurement> list);
}

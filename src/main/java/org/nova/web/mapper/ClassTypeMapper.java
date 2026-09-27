package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.gymclass.ClassType;
import org.nova.web.dto.request.gymclass.CreateClassTypeRequest;
import org.nova.web.dto.request.gymclass.UpdateClassTypeRequest;
import org.nova.web.dto.response.gymclass.ClassTypeResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClassTypeMapper {

    ClassTypeResponse toResponse(ClassType classType);

    List<ClassTypeResponse> toResponseList(List<ClassType> classTypes);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ClassType toEntity(CreateClassTypeRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(@MappingTarget ClassType entity, UpdateClassTypeRequest request);
}

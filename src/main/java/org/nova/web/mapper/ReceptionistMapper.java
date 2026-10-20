package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.staff.Receptionist;
import org.nova.web.dto.response.staff.ReceptionistResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReceptionistMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "users.firstName")
    @Mapping(target = "lastName", source = "users.lastName")
    @Mapping(target = "email", source = "users.email")
    @Mapping(target = "phone", source = "users.phone")
    @Mapping(target = "username", source = "users.username")
    @Mapping(target = "branchId", source = "users.branch.id")
    @Mapping(target = "branchName", source = "users.branch.name")
    @Mapping(target = "hireDate", source = "hireDate")
    @Mapping(target = "active", source = "users.active")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    ReceptionistResponse toResponse(Receptionist receptionist);

    List<ReceptionistResponse> toResponseList(List<Receptionist> receptionists);
}

package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.trainer.Trainer;
import org.nova.web.dto.response.trainer.TrainerResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrainerMapper {

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
    TrainerResponse toResponse(Trainer trainer);

    List<TrainerResponse> toResponseList(List<Trainer> trainers);
}

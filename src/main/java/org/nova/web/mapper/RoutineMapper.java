package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.workout.Routine;
import org.nova.web.dto.response.workout.RoutineResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoutineMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "assignmentId", source = "idAssignment.id")
    @Mapping(target = "memberId", source = "idAssignment.idMember.id")
    @Mapping(target = "memberName", expression = "java(r.getIdAssignment() != null && r.getIdAssignment().getIdMember() != null && r.getIdAssignment().getIdMember().getUsers() != null ? r.getIdAssignment().getIdMember().getUsers().getFirstName() + \" \" + r.getIdAssignment().getIdMember().getUsers().getLastName() : null)")
    @Mapping(target = "trainerId", source = "idAssignment.idTrainer.id")
    @Mapping(target = "trainerName", expression = "java(r.getIdAssignment() != null && r.getIdAssignment().getIdTrainer() != null && r.getIdAssignment().getIdTrainer().getUsers() != null ? r.getIdAssignment().getIdTrainer().getUsers().getFirstName() + \" \" + r.getIdAssignment().getIdTrainer().getUsers().getLastName() : null)")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    RoutineResponse toResponse(Routine r);

    List<RoutineResponse> toResponseList(List<Routine> list);
}

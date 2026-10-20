package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.trainer.TrainerAssignment;
import org.nova.web.dto.response.trainer.TrainerAssignmentResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrainerAssignmentMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "memberId", source = "idMember.id")
    @Mapping(target = "memberName", expression = "java(ta.getIdMember() != null && ta.getIdMember().getUsers() != null ? ta.getIdMember().getUsers().getFirstName() + \" \" + ta.getIdMember().getUsers().getLastName() : null)")
    @Mapping(target = "trainerId", source = "idTrainer.id")
    @Mapping(target = "trainerName", expression = "java(ta.getIdTrainer() != null && ta.getIdTrainer().getUsers() != null ? ta.getIdTrainer().getUsers().getFirstName() + \" \" + ta.getIdTrainer().getUsers().getLastName() : null)")
    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "endDate", source = "endDate")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "reason", source = "reason")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    TrainerAssignmentResponse toResponse(TrainerAssignment ta);

    List<TrainerAssignmentResponse> toResponseList(List<TrainerAssignment> list);
}

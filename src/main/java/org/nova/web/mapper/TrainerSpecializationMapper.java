package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.trainer.TrainerSpecialization;
import org.nova.web.dto.response.trainer.TrainerSpecializationResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrainerSpecializationMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "trainerId", source = "idTrainer.id")
    @Mapping(target = "trainerName", expression = "java(ts.getIdTrainer() != null && ts.getIdTrainer().getUsers() != null ? ts.getIdTrainer().getUsers().getFirstName() + \" \" + ts.getIdTrainer().getUsers().getLastName() : null)")
    @Mapping(target = "specializationId", source = "idSpecialization.id")
    @Mapping(target = "specializationName", source = "idSpecialization.name")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    TrainerSpecializationResponse toResponse(TrainerSpecialization ts);

    List<TrainerSpecializationResponse> toResponseList(List<TrainerSpecialization> list);
}

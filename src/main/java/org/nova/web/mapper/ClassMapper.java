package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.gymclass.Class;
import org.nova.domain.model.trainer.Trainer;
import org.nova.web.dto.response.gymclass.ClassResponse;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClassMapper {

    @Mapping(target = "id", source = "c.id")
    @Mapping(target = "branchId", source = "c.idBranch.id")
    @Mapping(target = "branchName", source = "c.idBranch.name")
    @Mapping(target = "classTypeId", source = "c.idClassType.id")
    @Mapping(target = "classTypeName", source = "c.idClassType.name")
    @Mapping(target = "trainerId", source = "c.idTrainer.id")
    @Mapping(target = "trainerName", source = "c.idTrainer", qualifiedByName = "trainerFullName")
    @Mapping(target = "status", source = "c.status")
    @Mapping(target = "schedule", source = "c.schedule")
    @Mapping(target = "duration", source = "c.duration")
    @Mapping(target = "maxCapacity", source = "c.maxCapacity")
    @Mapping(target = "enrolledCount", source = "enrolledCount")
    @Mapping(target = "availableSpots", expression = "java(calculateAvailableSpots(c.getMaxCapacity(), enrolledCount))")
    @Mapping(target = "waitlistCount", source = "waitlistCount")
    @Mapping(target = "averageRating", source = "averageRating")
    @Mapping(target = "createdAt", source = "c.createdAt")
    @Mapping(target = "updatedAt", source = "c.updatedAt")
    ClassResponse toResponse(Class c, Long enrolledCount, Long waitlistCount, Double averageRating);

    @Named("trainerFullName")
    default String trainerFullName(Trainer trainer) {
        if (trainer == null || trainer.getUsers() == null) {
            return null;
        }
        String first = trainer.getUsers().getFirstName() != null ? trainer.getUsers().getFirstName() : "";
        String last = trainer.getUsers().getLastName() != null ? trainer.getUsers().getLastName() : "";
        return (first + " " + last).trim();
    }

    default Long calculateAvailableSpots(Long maxCapacity, Long enrolledCount) {
        if (maxCapacity == null) return 0L;
        long enrolled = enrolledCount != null ? enrolledCount : 0L;
        long diff = maxCapacity - enrolled;
        return diff > 0 ? diff : 0L;
    }
}

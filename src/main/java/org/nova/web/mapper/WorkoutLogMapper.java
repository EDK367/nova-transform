package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.workout.WorkoutLog;
import org.nova.web.dto.response.workout.WorkoutLogResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WorkoutLogMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "memberId", source = "idMember.id")
    @Mapping(target = "memberName", expression = "java(wl.getIdMember() != null && wl.getIdMember().getUsers() != null ? wl.getIdMember().getUsers().getFirstName() + \" \" + wl.getIdMember().getUsers().getLastName() : null)")
    @Mapping(target = "routineExerciseId", source = "idRoutineExercise.id")
    @Mapping(target = "exerciseId", source = "idRoutineExercise.idExercise.id")
    @Mapping(target = "exerciseName", source = "idRoutineExercise.idExercise.name")
    @Mapping(target = "routineName", source = "idRoutineExercise.idRoutine.name")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "actualSets", source = "actualSets")
    @Mapping(target = "actualReps", source = "actualReps")
    @Mapping(target = "actualWeight", source = "actualWeight")
    @Mapping(target = "observation", source = "observation")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    WorkoutLogResponse toResponse(WorkoutLog wl);

    List<WorkoutLogResponse> toResponseList(List<WorkoutLog> list);
}

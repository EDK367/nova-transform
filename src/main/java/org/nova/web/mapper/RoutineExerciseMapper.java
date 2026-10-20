package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.workout.RoutineExercise;
import org.nova.web.dto.response.workout.RoutineExerciseResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoutineExerciseMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "routineId", source = "idRoutine.id")
    @Mapping(target = "routineName", source = "idRoutine.name")
    @Mapping(target = "exerciseId", source = "idExercise.id")
    @Mapping(target = "exerciseName", source = "idExercise.name")
    @Mapping(target = "muscleGroup", source = "idExercise.muscleGroup")
    @Mapping(target = "trainingDay", source = "trainingDay")
    @Mapping(target = "sets", source = "sets")
    @Mapping(target = "reps", source = "reps")
    @Mapping(target = "weight", source = "weight")
    @Mapping(target = "rest", source = "rest")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    RoutineExerciseResponse toResponse(RoutineExercise re);

    List<RoutineExerciseResponse> toResponseList(List<RoutineExercise> list);
}

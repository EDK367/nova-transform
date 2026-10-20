package org.nova.service.workout;

import org.nova.domain.model.workout.RoutineExercise;
import org.nova.web.dto.request.workout.CreateRoutineExerciseRequest;
import org.nova.web.dto.request.workout.UpdateRoutineExerciseRequest;
import org.nova.web.dto.response.workout.RoutineExerciseResponse;

import java.util.List;
import java.util.Optional;

public interface RoutineExerciseService {

    List<RoutineExercise> findAll();

    List<RoutineExerciseResponse> findAllResponses(Long routineId, String trainingDay);

    Optional<RoutineExercise> findById(Long id);

    RoutineExerciseResponse findResponseById(Long id);

    RoutineExercise create(RoutineExercise routineExercise);

    RoutineExerciseResponse createRoutineExercise(CreateRoutineExerciseRequest request);

    RoutineExercise update(Long id, RoutineExercise routineExercise);

    RoutineExerciseResponse updateRoutineExercise(Long id, UpdateRoutineExerciseRequest request);

    void delete(Long id);

    void deleteRoutineExercise(Long id);
}

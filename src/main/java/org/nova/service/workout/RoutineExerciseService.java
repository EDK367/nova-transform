package org.nova.service.workout;

import org.nova.domain.model.workout.RoutineExercise;

import java.util.List;
import java.util.Optional;

public interface RoutineExerciseService {

    List<RoutineExercise> findAll();

    Optional<RoutineExercise> findById(Long id);

    RoutineExercise create(RoutineExercise routineExercise);

    RoutineExercise update(Long id, RoutineExercise routineExercise);

    void delete(Long id);
}
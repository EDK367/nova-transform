package org.nova.service.workout;

import org.nova.domain.model.workout.Exercise;
import org.nova.web.dto.request.workout.CreateExerciseRequest;
import org.nova.web.dto.request.workout.UpdateExerciseRequest;
import org.nova.web.dto.response.workout.ExerciseResponse;

import java.util.List;
import java.util.Optional;

public interface ExerciseService {

    List<Exercise> findAll();

    List<ExerciseResponse> findAllResponses(String muscleGroup);

    Optional<Exercise> findById(Long id);

    ExerciseResponse findResponseById(Long id);

    Exercise create(Exercise exercise);

    ExerciseResponse createExercise(CreateExerciseRequest request);

    Exercise update(Long id, Exercise exercise);

    ExerciseResponse updateExercise(Long id, UpdateExerciseRequest request);

    void delete(Long id);

    void deleteExercise(Long id);
}

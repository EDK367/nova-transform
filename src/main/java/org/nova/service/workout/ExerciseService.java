package org.nova.service.workout;

import org.nova.domain.model.workout.Exercise;

import java.util.List;
import java.util.Optional;

public interface ExerciseService {

    List<Exercise> findAll();

    Optional<Exercise> findById(Long id);

    Exercise create(Exercise exercise);

    Exercise update(Long id, Exercise exercise);

    void delete(Long id);
}
package org.nova.service.workout;

import org.nova.domain.model.workout.WorkoutLog;

import java.util.List;
import java.util.Optional;

public interface WorkoutLogService {

    List<WorkoutLog> findAll();

    Optional<WorkoutLog> findById(Long id);

    WorkoutLog create(WorkoutLog workoutLog);

    WorkoutLog update(Long id, WorkoutLog workoutLog);

    void delete(Long id);
}
package org.nova.service.workout;

import org.nova.domain.model.workout.WorkoutLog;
import org.nova.web.dto.request.workout.CreateWorkoutLogRequest;
import org.nova.web.dto.request.workout.UpdateWorkoutLogRequest;
import org.nova.web.dto.response.workout.WorkoutLogResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkoutLogService {

    List<WorkoutLog> findAll();

    List<WorkoutLogResponse> findAllResponses(UUID memberId, Long routineExerciseId);

    Optional<WorkoutLog> findById(Long id);

    WorkoutLogResponse findResponseById(Long id);

    WorkoutLog create(WorkoutLog workoutLog);

    WorkoutLogResponse createWorkoutLog(CreateWorkoutLogRequest request);

    WorkoutLog update(Long id, WorkoutLog workoutLog);

    WorkoutLogResponse updateWorkoutLog(Long id, UpdateWorkoutLogRequest request);

    void delete(Long id);

    void deleteWorkoutLog(Long id);
}

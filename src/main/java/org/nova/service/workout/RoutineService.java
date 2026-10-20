package org.nova.service.workout;

import org.nova.domain.model.workout.Routine;
import org.nova.web.dto.request.workout.CreateRoutineRequest;
import org.nova.web.dto.request.workout.UpdateRoutineRequest;
import org.nova.web.dto.response.workout.RoutineResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoutineService {

    List<Routine> findAll();

    List<RoutineResponse> findAllResponses(Long assignmentId, UUID memberId);

    Optional<Routine> findById(Long id);

    RoutineResponse findResponseById(Long id);

    Routine create(Routine routine);

    RoutineResponse createRoutine(CreateRoutineRequest request);

    Routine update(Long id, Routine routine);

    RoutineResponse updateRoutine(Long id, UpdateRoutineRequest request);

    void delete(Long id);

    void deleteRoutine(Long id);
}

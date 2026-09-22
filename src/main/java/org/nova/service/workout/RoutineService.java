package org.nova.service.workout;

import org.nova.domain.model.workout.Routine;

import java.util.List;
import java.util.Optional;

public interface RoutineService {

    List<Routine> findAll();

    Optional<Routine> findById(Long id);

    Routine create(Routine routine);

    Routine update(Long id, Routine routine);

    void delete(Long id);
}
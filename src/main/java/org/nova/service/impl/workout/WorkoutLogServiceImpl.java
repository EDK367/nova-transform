package org.nova.service.impl.workout;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.workout.WorkoutLog;
import org.nova.repository.WorkoutLogRepository;
import org.nova.service.workout.WorkoutLogService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class WorkoutLogServiceImpl implements WorkoutLogService {

    private final WorkoutLogRepository repository;

    @Override
    public List<WorkoutLog> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<WorkoutLog> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public WorkoutLog create(WorkoutLog workoutLog) {
        repository.persist(workoutLog);
        return workoutLog;
    }

    @Override
    @Transactional
    public WorkoutLog update(Long id, WorkoutLog workoutLog) {
        WorkoutLog existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setIdMember(workoutLog.getIdMember());
        existing.setIdRoutineExercise(workoutLog.getIdRoutineExercise());
        existing.setDate(workoutLog.getDate());
        existing.setActualSets(workoutLog.getActualSets());
        existing.setActualReps(workoutLog.getActualReps());
        existing.setActualWeight(workoutLog.getActualWeight());
        existing.setObservation(workoutLog.getObservation());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}